package com.example.rapidrescue.ui.screens.authentication.registration

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidrescue.data.models.UserModel
import com.example.rapidrescue.data.repositories.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.DriverManager.println


sealed class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {

    val authRepository = AuthRepository()

    //     state
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _message = MutableStateFlow("")
    val message = _message.asStateFlow()


    //     methods
    fun registerUser(userModel: UserModel) {
        _isLoading.value = true
        viewModelScope.launch {

            try {
                authRepository.registerUser(userModel)
                _isLoading.value =false
                _message.value="success!"
            }catch (e:Error){
                _isLoading.value =false
                _message.value="Oops! Something went wrong:${e.message}"
            }

        }
    }
}