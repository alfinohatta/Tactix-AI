package com.example.tactixai.di

import android.content.Context
import com.example.tactixai.data.local.TactixDatabase
import com.example.tactixai.data.remote.TactixRetrofitClient
import com.example.tactixai.data.repository.*
import com.example.tactixai.core.network.CloudSyncManager
import com.example.tactixai.core.network.TactixNetworkManager
import okhttp3.OkHttpClient

/**
 * AppContainer: Pusat Injeksi Dependensi Platform.
 * Mengelola seluruh koneksi ke MySQL dan Ekosistem SimOS.
 */
class AppContainer(context: Context) {

    private val db = TactixDatabase.getDatabase(context)
    private val apiService = TactixRetrofitClient.instance
    
    // Repositories
    val tactixRepository = TactixRepository(db.simulationDao(), db.agentDao(), apiService)
    val marketplaceRepository = MarketplaceRepository(apiService)
    val authRepository = AuthRepository(apiService)
    val replayRepository = ReplayRepository(db.simulationDao())

    // Networking (MySQL Bridge)
    val cloudSyncManager = CloudSyncManager(apiService)
    val networkManager = TactixNetworkManager(OkHttpClient())

    companion object {
        @Volatile
        private var instance: AppContainer? = null
        fun getInstance(context: Context): AppContainer {
            return instance ?: synchronized(this) {
                AppContainer(context).also { instance = it }
            }
        }
    }
}
