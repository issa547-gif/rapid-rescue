package com.example.rapidrescue.ui.screens.alerts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String
)

class AlertsViewModel : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts = _alerts.asStateFlow()

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        _alerts.value = listOf(
            Alert(
                id = "1",
                title = "SOS Triggered",
                description = "Emergency alert sent from home location",
                timestamp = formatter.format(Date(System.currentTimeMillis() - 3600000))
            ),
            Alert(
                id = "2",
                title = "SOS Triggered",
                description = "Emergency alert sent from current location",
                timestamp = formatter.format(Date())
            )
        )
    }

    fun addAlert(description: String) {
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val newAlert = Alert(
            id = System.currentTimeMillis().toString(),
            title = "SOS Triggered",
            description = description,
            timestamp = formatter.format(Date())
        )
        _alerts.value = listOf(newAlert) + _alerts.value
    }
}