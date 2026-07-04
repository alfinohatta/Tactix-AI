package com.example.tactixai.data.repository

import com.example.tactixai.core.model.SimulationBlueprint
import com.example.tactixai.data.remote.TactixApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Layer 14: AI Marketplace Repository (Production Version).
 * Mengelola pertukaran Blueprint antara pengguna lokal dan Cloud MySQL.
 */
class MarketplaceRepository(private val apiService: TactixApiService) {

    private val localCache = ConcurrentHashMap<String, SimulationBlueprint>()

    /**
     * Mengambil daftar Blueprint dari MySQL global untuk ditampilkan di UI.
     */
    suspend fun fetchGlobalMarketplace(): List<SimulationBlueprint> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPublicBlueprints()
            if (response.isSuccessful) {
                return@withContext response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            // Log error
        }
        return@withContext emptyList()
    }

    /**
     * Mengunggah desain ekosistem user ke Marketplace Cloud.
     */
    suspend fun publishToCloud(blueprint: SimulationBlueprint): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.publishBlueprint(blueprint)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Menyimpan Blueprint yang diunduh ke cache lokal untuk dijalankan di SimOS.
     */
    fun saveToLocalCache(blueprint: SimulationBlueprint) {
        localCache[blueprint.name] = blueprint
    }
}
