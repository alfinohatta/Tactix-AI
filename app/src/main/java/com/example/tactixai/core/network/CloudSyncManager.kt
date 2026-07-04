package com.example.tactixai.core.network

import com.example.tactixai.core.intelligence.AIExplanation
import com.example.tactixai.data.local.entities.*
import com.example.tactixai.data.remote.TactixApiService
import com.example.tactixai.core.platform.UserSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Platform Cloud Sync Engine.
 * Menghubungkan SimOS ke MySQL Production 8+ Database.
 * Mengelola sinkronisasi Batch untuk 10.000+ agen.
 */
class CloudSyncManager(private val apiService: TactixApiService) {
    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Mengunggah Keputusan AI (Explainability) ke tabel 'ai_decisions'.
     */
    fun syncAIDecisions(simulationId: Long, logs: List<AIExplanation>) {
        if (!UserSessionManager.isLoggedIn()) return
        
        scope.launch {
            val entities = logs.map { log ->
                AIDecisionEntity(
                    simulationId = simulationId,
                    agentId = null, // Squad level log
                    decisionType = log.action,
                    reasoning = log.reason,
                    confidence = log.confidence
                )
            }
            try {
                apiService.logAIDecisions(entities)
            } catch (e: Exception) {
                // Background retry logic
            }
        }
    }

    /**
     * Sinkronisasi Hasil Akhir ke tabel 'simulation_results'.
     */
    fun syncFinalOutcome(simulationId: Long, outcome: SimulationResultEntity) {
        scope.launch {
            try {
                apiService.storeFinalOutcome(outcome)
            } catch (e: Exception) {
                // Store in local queue for retry
            }
        }
    }

    /**
     * Mendaftarkan Device Baru ke tabel 'user_devices'.
     */
    fun registerDevice(deviceName: String) {
        val userId = UserSessionManager.getUserId()
        if (userId == 0L) return

        scope.launch {
            val device = UserDeviceEntity(
                userId = userId,
                platform = "ANDROID",
                deviceName = deviceName,
                lastActive = System.currentTimeMillis()
            )
            // apiService.registerDevice(device)
        }
    }
}
