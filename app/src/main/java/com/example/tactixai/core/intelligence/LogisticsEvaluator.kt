package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.AgentStatus

/**
 * Layer 5: Analytics Engine - Logistics Module.
 * Mengevaluasi efisiensi operasional untuk skenario B2B (Warehouse/Factory).
 */
class LogisticsEvaluator {

    data class LogisticsMetrics(
        val throughput: Float,      // Tasks completed per minute
        val collisionRisk: Float,   // Proximity warnings
        val energyEfficiency: Float // Energy used per distance
    )

    fun calculateWarehouseEfficiency(robots: List<Agent>): LogisticsMetrics {
        val totalRobots = robots.size
        if (totalRobots == 0) return LogisticsMetrics(0f, 0f, 0f)

        // Hitung rata-rata Confidence sebagai proxy untuk stabilitas sistem
        val systemStability = robots.map { it.confidence }.average().toFloat()
        
        // Collision Risk berdasarkan kedekatan antar robot (menggunakan aiState)
        val proximityWarnings = robots.count { it.aiState["near_collision"] == true }
        val risk = proximityWarnings.toFloat() / totalRobots

        return LogisticsMetrics(
            throughput = systemStability * 100f,
            collisionRisk = risk,
            energyEfficiency = robots.map { it.energy }.average().toFloat() / 100f
        )
    }
}
