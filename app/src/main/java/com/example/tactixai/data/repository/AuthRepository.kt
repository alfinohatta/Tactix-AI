package com.example.tactixai.data.repository

import com.example.tactixai.data.local.entities.UserEntity
import com.example.tactixai.data.local.entities.UserDeviceEntity
import com.example.tactixai.data.remote.TactixApiService
import com.example.tactixai.core.platform.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Mengelola Autentikasi dan Registrasi Perangkat.
 * Selaras dengan tabel 'users' dan 'user_devices' di MySQL Production.
 */
class AuthRepository(private val apiService: TactixApiService) {

    /**
     * Mendaftarkan user baru ke MySQL.
     */
    suspend fun signUp(email: String, username: String, passHash: String): Boolean = withContext(Dispatchers.IO) {
        val user = UserEntity(email = email, username = username, passwordHash = passHash)
        val response = apiService.registerUser(user)
        if (response.isSuccessful) {
            response.body()?.let { UserSessionManager.login(it) }
            return@withContext true
        }
        return@withContext false
    }

    /**
     * Mendaftarkan perangkat Android ke MySQL untuk sinkronisasi multi-device.
     */
    suspend fun registerCurrentDevice(deviceName: String) = withContext(Dispatchers.IO) {
        val userId = UserSessionManager.getUserId()
        if (userId == 0L) return@withContext
        
        val device = UserDeviceEntity(
            userId = userId,
            platform = "ANDROID",
            deviceName = deviceName,
            lastActive = System.currentTimeMillis()
        )
        // apiService.registerDevice(device)
    }

    /**
     * Sinkronisasi status paket (Plan) dari MySQL.
     */
    suspend fun refreshSubscription() = withContext(Dispatchers.IO) {
        val userId = UserSessionManager.getUserId()
        val response = apiService.getSubscriptionStatus(userId)
        if (response.isSuccessful) {
            // Update UserSessionManager dengan plan terbaru (FREE/PRO/ENTERPRISE)
        }
    }
}
