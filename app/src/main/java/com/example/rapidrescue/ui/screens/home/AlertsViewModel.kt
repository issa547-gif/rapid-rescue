package com.example.rapidrescue.ui.screens.home

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidrescue.data.locationhelper.getCurrentLocation
import com.example.rapidrescue.data.repositories.AuthRepository
import com.example.rapidrescue.data.repositories.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class SOSState {
    object Idle : SOSState()
    object Loading : SOSState()
    data class Success(val lat: Double, val lng: Double) : SOSState()
    data class Error(val message: String) : SOSState()
}

class AlertViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val contactRepository = ContactRepository()

    private val _sosState = MutableStateFlow<SOSState>(SOSState.Idle)
    val sosState = _sosState.asStateFlow()

    private var alertAlreadySent = false

//    fun triggerSOS(context: Context) {
//        if (alertAlreadySent) return // prevent duplicate sends
//        viewModelScope.launch {
//            _sosState.value = SOSState.Loading
//            alertAlreadySent = true
//            try {
//                val (lat, lng) = getCurrentLocation(context)
//                repository.insertAlert(lat, lng)
//                sendSMSAlerts(context, lat, lng)
//                _sosState.value = SOSState.Success(lat, lng)
//            } catch (e: Exception) {
//                _sosState.value = SOSState.Error(e.message ?: "Failed to send alert")
//                alertAlreadySent = false
//            }
//        }
//    }
//    fun triggerSOS(context: Context) {
//        // check GPS is on before trying
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
//        val isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
//        val isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
//
//        if (!isGpsEnabled && !isNetworkEnabled) {
//            _sosState.value = SOSState.Error("Please enable GPS to send SOS")
//            return
//        }
//
//        if (alertAlreadySent) return
//        viewModelScope.launch {
//            _sosState.value = SOSState.Loading
//            alertAlreadySent = true
//            try {
//                val (lat, lng) = getCurrentLocation(context)
//                repository.insertAlert(lat, lng)
//                sendSMSAlerts(context, lat, lng)
//                _sosState.value = SOSState.Success(lat, lng)
//            } catch (e: Exception) {
//                _sosState.value = SOSState.Error(e.message ?: "Failed to send alert")
//                alertAlreadySent = false
//            }
//        }
//    }

    fun triggerSOS(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE)
                as android.location.LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(
            android.location.LocationManager.GPS_PROVIDER
        )
        val isNetworkEnabled = locationManager.isProviderEnabled(
            android.location.LocationManager.NETWORK_PROVIDER
        )

        if (!isGpsEnabled && !isNetworkEnabled) {
            // open location settings so user can enable GPS
            val intent = android.content.Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
            ).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            _sosState.value = SOSState.Error("GPS disabled — please enable and try again")
            return
        }

        if (alertAlreadySent) return
        viewModelScope.launch {
            _sosState.value = SOSState.Loading
            alertAlreadySent = true
            try {
                val (lat, lng) = getCurrentLocation(context)
                repository.insertAlert(lat, lng)
                sendSMSAlerts(context, lat, lng)
                _sosState.value = SOSState.Success(lat, lng)
            } catch (e: Exception) {
                _sosState.value = SOSState.Error(e.message ?: "Failed to send alert")
                alertAlreadySent = false
            }
        }
    }

    private suspend fun sendSMSAlerts(context: Context, lat: Double, lng: Double) {
        val contacts = contactRepository.getContacts()

        if (contacts.isEmpty()) {
            throw Exception("No emergency contacts found. Add contacts first.")
        }

        val mapsLink = "https://maps.google.com/?q=$lat,$lng"
        val time = SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault()).format(Date())
        val message = buildString {
            append("🚨 EMERGENCY ALERT - RapidRescue\n\n")
            append("I need immediate help!\n\n")
            append("📍 Location: $mapsLink\n")
            append("🕐 Time: $time\n\n")
            append("Please call me or send help immediately.")
        }

        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }

        contacts.forEach { contact ->
            try {
                // split into multiple parts if message is too long
                val parts = smsManager?.divideMessage(message)
                if (parts != null && parts.size > 1) {
                    smsManager.sendMultipartTextMessage(
                        contact.phone,
                        null,
                        parts,
                        null,
                        null
                    )
                } else {
                    smsManager?.sendTextMessage(
                        contact.phone,
                        null,
                        message,
                        null,
                        null
                    )
                }
            } catch (e: Exception) {
                // continue to next contact if one fails
            }
        }
    }

    fun resetState() {
        _sosState.value = SOSState.Idle
        alertAlreadySent = false
    }
}