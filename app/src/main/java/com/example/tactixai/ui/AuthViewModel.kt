package com.example.tactixai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tactixai.data.repository.AuthRepository
import com.example.tactixai.core.platform.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Mengelola interaksi Auth antara UI dan MySQL Cloud.
 * Realisasi dari poin 'MySQL Identity Bridge'.
 */
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState = _authState.asStateFlow()

    fun performLogin(email: String, username: String, hash: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val success = authRepository.signUp(email, username, hash)
            if (success) {
                authRepository.registerCurrentDevice(android.os.Build.MODEL)
                _authState.value = AuthState.LoggedIn
            } else {
                _authState.value = AuthState.Error("Koneksi MySQL Gagal")
            }
        }
    }

    sealed class AuthState {
        object LoggedOut : AuthState()
        object Loading : AuthState()
        object LoggedIn : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
