package com.example.tactixai.core.analytics

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.AgentStatus
import org.json.JSONObject

/**
 * Layer 5: Analytics Engine.
 * Menghasilkan 'Outcome Analysis' mendalam setelah simulasi berakhir.
 * Data ini digunakan untuk Reinforcement Learning (Point 5).
 */
class OutcomeAnalysisEngine {

    data class SimulationOutcome(
        val successRate: Float,
        val efficiencyScore: Float,
        val survivalRate: Float,
        val resourceUsageJson: String,
        val aiSummary: String
    )

    fun analyze(initialCount: Int, finalAgents: List<Agent>): SimulationOutcome {
        val aliveCount = finalAgents.count { it.status != AgentStatus.DESTROYED }
        val survivalRate = (aliveCount.toFloat() / initialCount.toFloat()).coerceIn(0f, 1f)
        
        val totalHealth = finalAgents.sumOf { it.health }.toFloat()
        val avgHealth = if (finalAgents.isNotEmpty()) totalHealth / finalAgents.size else 0f
        
        // Efficiency Score: Gabungan survival dan sisa kesehatan
        val efficiency = (survivalRate * 0.7f + (avgHealth / 100f) * 0.3f)
        
        // Resource Usage Simulation
        val resourceUsage = JSONObject().apply {
            put("energy_consumed", finalAgents.sumOf { 100 - it.energy })
            put("information_spent", (1.0f - survivalRate) * 1000) // Proxy
        }

        val aiSummary = generateNarrative(survivalRate, efficiency)

        return SimulationOutcome(
            successRate = if (survivalRate > 0.5f) 1.0f else 0f,
            efficiencyScore = efficiency,
            survivalRate = survivalRate,
            resourceUsageJson = resourceUsage.toString(),
            aiSummary = aiSummary
        )
    }

    private fun generateNarrative(survival: Float, efficiency: Float): String {
        return when {
            survival > 0.8f -> "Dominasi total. Formasi yang dipilih sangat efektif dalam memitigasi ancaman."
            survival > 0.5f -> "Misi berhasil dengan kerugian moderat. Perlu optimasi pada lapisan pertahanan."
            else -> "Kegagalan taktis. Formasi gagal beradaptasi dengan densitas ancaman lingkungan."
        }
    }
}
