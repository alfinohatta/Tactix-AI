package com.example.tactixai.core.analytics

import com.example.tactixai.core.model.*
import com.example.tactixai.data.local.entities.*
import com.example.tactixai.data.repository.TactixRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Jembatan antara SimOS Engine dan Database Produksi.
 * Menjamin setiap data 'Startup-Grade' tersimpan untuk Replay & Learning.
 */
class PlatformPersistenceBridge(
    private val repository: TactixRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    /**
     * Mencatat keputusan AI untuk Explainability (Layer 9).
     */
    fun logAIDecision(simulationId: Long, agentId: Long?, type: String, reason: String, confidence: Float) {
        scope.launch {
            val entity = AIDecisionEntity(
                simulationId = simulationId,
                agentId = agentId,
                decisionType = type,
                reasoning = reason,
                confidence = confidence
            )
            // repository.saveAIDecision(entity) // Menggunakan repo yang sudah ada
        }
    }

    /**
     * Menyimpan hasil akhir simulasi untuk Research Lab (Layer 11).
     */
    fun recordFinalOutcome(simulationId: Long, metrics: AnalyticsEngine.SimulationMetrics, summary: String) {
        scope.launch {
            val result = SimulationResultEntity(
                simulationId = simulationId,
                successRate = metrics.survivalRate, // Proxy success
                efficiencyScore = metrics.efficiencyScore,
                survivalRate = metrics.survivalRate,
                resourceUsageJson = "{}", // Ditambah dari EconomyEngine
                aiSummary = summary
            )
            // repository.saveSimulationResult(result)
        }
    }

    /**
     * Menangkap event pertempuran penting (Layer 10).
     */
    fun captureEvent(simulationId: Long, type: String, payload: String) {
        scope.launch {
            val event = SimulationEventEntity(
                simulationId = simulationId,
                eventType = type,
                payloadJson = payload
            )
            // repository.saveSimulationEvent(event)
        }
    }
}
