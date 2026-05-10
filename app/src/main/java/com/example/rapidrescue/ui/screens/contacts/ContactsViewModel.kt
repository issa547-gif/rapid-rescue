package com.example.rapidrescue.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidrescue.data.models.Contact
import com.example.rapidrescue.data.repositories.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ContactsState {
    object Idle : ContactsState()
    object Loading : ContactsState()
    object Success : ContactsState()
    data class Error(val message: String) : ContactsState()
}

class ContactsViewModel : ViewModel() {

    private val repository = ContactRepository()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _state = MutableStateFlow<ContactsState>(ContactsState.Idle)
    val state = _state.asStateFlow()

    init {
        loadContacts()
    }

    fun loadContacts() {
        viewModelScope.launch {
            _state.value = ContactsState.Loading
            try {
                _contacts.value = repository.getContacts()
                _state.value = ContactsState.Idle
            } catch (e: Exception) {
                _state.value = ContactsState.Error(e.message ?: "Failed to load contacts")
            }
        }
    }

    fun addContact(name: String, phone: String, relationship: String) {
        if (name.isBlank() || phone.isBlank()) {
            _state.value = ContactsState.Error("Name and phone are required")
            return
        }
        viewModelScope.launch {
            _state.value = ContactsState.Loading
            try {
                repository.addContact(
                    Contact(name = name, phone = phone, relationship = relationship)
                )
                loadContacts()
                _state.value = ContactsState.Success
            } catch (e: Exception) {
                _state.value = ContactsState.Error(e.message ?: "Failed to add contact")
            }
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteContact(id)
                _contacts.value = _contacts.value.filter { it.id != id }
            } catch (e: Exception) {
                _state.value = ContactsState.Error(e.message ?: "Failed to delete contact")
            }
        }
    }

    fun resetState() {
        _state.value = ContactsState.Idle
    }
}