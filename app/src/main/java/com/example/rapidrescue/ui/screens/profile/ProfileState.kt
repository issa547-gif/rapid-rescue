package com.example.rapidrescue.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidrescue.data.models.Profile
import com.example.rapidrescue.data.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    object Success : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state = _state.asStateFlow()

    val userEmail = repository.getCurrentUserEmail()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                _profile.value = repository.getProfile()
                _state.value = ProfileState.Idle
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }
}