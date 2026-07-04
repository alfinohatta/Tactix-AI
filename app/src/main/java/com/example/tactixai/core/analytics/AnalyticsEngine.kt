package com.example.tactixai.core.analytics

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.AgentStatus

/**
 * Analytics Engine Platform (Layer 5).
 * Menghasilkan metrik real-time untuk 10.000+ agen.
 */
class AnalyticsEngine {

    data class SimulationMetrics(
        val survivalRate: Float,
        val efficiencyScore: Float,
        val combatEffectiveUnits: Int,
        val totalEnergyConsumed: Float,
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Kalkulasi metrik dari state agen saat ini (DOD Friendly).
     */
    fun calculateMetrics(agents: List<Agent>): SimulationMetrics {
        if (agents.isEmpty()) return SimulationMetrics(0f, 0f, 0, 0f)

        val activeAgents = agents.count { it.status != AgentStatus.DESTROYED }
        val survivalRate = activeAgents.toFloat() / agents.size
        
        // Proxy efficiency: rata-rata energi sisa agen yang hidup
        val avgEnergy = agents.filter { it.status != AgentStatus.DESTROYED }
            .map { it.energy }.average().toFloat()

        return SimulationMetrics(
            survivalRate = survivalRate,
            efficiencyScore = avgEnergy / 100f,
            combatEffectiveUnits = activeAgents,
            totalEnergyConsumed = agents.sumOf { 100 - it.energy }.toFloat()
        )
    }
}
