package com.example.rapidrescue.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidrescue.data.models.AlertModel
import com.example.rapidrescue.data.repositories.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AlertsState {
    object Loading : AlertsState()
    object Empty : AlertsState()
    object Idle : AlertsState()
    data class Error(val message: String) : AlertsState()
}

class AlertsViewModel : ViewModel() {

    private val repository = AlertRepository()

    private val _alerts = MutableStateFlow<List<AlertModel>>(emptyList())
    val alerts = _alerts.asStateFlow()

    private val _state = MutableStateFlow<AlertsState>(AlertsState.Idle)
    val state = _state.asStateFlow()

    init {
        loadAlerts()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _state.value = AlertsState.Loading
            try {
                val result = repository.getAlerts()
                _alerts.value = result
                _state.value = if (result.isEmpty()) AlertsState.Empty else AlertsState.Idle
            } catch (e: Exception) {
                _state.value = AlertsState.Error(e.message ?: "Failed to load alerts")
            }
        }
    }
}