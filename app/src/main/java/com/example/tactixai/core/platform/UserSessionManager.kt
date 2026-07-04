package com.example.tactixai.core.platform

import com.example.tactixai.data.local.entities.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mengelola identitas pengguna yang aktif di platform.
 * Selaras dengan tabel 'users' di MySQL Production.
 */
object UserSessionManager {
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun login(user: UserEntity) {
        _currentUser.value = user
    }

    fun getUserId(): Long = _currentUser.value?.id ?: 0L
    fun getPlan(): String = _currentUser.value?.plan ?: "FREE"
    
    fun isLoggedIn(): Boolean = _currentUser.value != null
}
