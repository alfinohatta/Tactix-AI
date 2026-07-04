package com.example.tactixai.core.api

import com.example.tactixai.core.SimulationManager
import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.intelligence.AIExplanation
import com.example.tactixai.core.analytics.AnalyticsEngine
import kotlinx.coroutines.flow.StateFlow

/**
 * Pintu gerbang utama untuk pengembang pihak ketiga (API Layer).
 * Memungkinkan integrasi B2B untuk pengujian sistem otonom.
 */
class TactixPlatformAPI(private val manager: SimulationManager) {

    /**
     * Mendapatkan stream data agen secara real-time.
     */
    fun getAgentStream(): StateFlow<List<Agent>> = manager.agents

    /**
     * Mengambil log keputusan strategis terbaru.
     */
    fun getStrategicInsights(): List<com.example.tactixai.core.intelligence.AIExplanation> = manager.getStrategicLogs()

    /**
     * Mengambil laporan performa terbaru.
     */
    fun getPerformanceReport(): AnalyticsEngine.SimulationMetrics = manager.getPerformanceReport()

    /**
     * Ekspor data simulasi ke format JSON untuk analitik eksternal.
     */
    fun exportSimulationData(): String {
        val outcome = manager.getPerformanceReport()
        return "{ \"survival\": ${outcome.survivalRate}, \"efficiency\": ${outcome.efficiencyScore} }"
    }
}
