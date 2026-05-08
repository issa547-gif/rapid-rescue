package com.example.rapidrescue.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidrescue.data.locationhelper.getCurrentLocation
import com.example.rapidrescue.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SOSState {
    object Idle : SOSState()
    object Loading : SOSState()
    data class Success(val lat: Double, val lng: Double) : SOSState()
    data class Error(val message: String) : SOSState()
}

class AlertViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _sosState = MutableStateFlow<SOSState>(SOSState.Idle)
    val sosState = _sosState.asStateFlow()

    fun triggerSOS(context: Context) {
        viewModelScope.launch {
            _sosState.value = SOSState.Loading
            try {
                val (lat, lng) = getCurrentLocation(context)
                repository.insertAlert(lat, lng)
                _sosState.value = SOSState.Success(lat, lng)
            } catch (e: Exception) {
                _sosState.value = SOSState.Error(e.message ?: "Failed to get location")
            }
        }
    }

    fun resetState() {
        _sosState.value = SOSState.Idle
    }
}