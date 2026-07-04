package com.example.tactixai.core.engine

import com.example.tactixai.core.model.AgentDomain
import com.example.tactixai.core.model.GlobalRule

/**
 * Layer 1: Rules Engine (General-Purpose v2).
 * Menerapkan hukum dunia yang spesifik untuk Industri (B2B) dan Militer.
 */
class RulesEngine(private val rules: List<GlobalRule>) {

    fun applyRulesToBuffer(buffer: AgentStateBuffer, deltaTime: Float, industryType: String) {
        for (i in 0 until buffer.capacity) {
            if (!buffer.active[i]) continue

            // 1. Domain-Specific Physics
            applyPhysicsByIndustry(buffer, i, deltaTime, industryType)

            // 2. Global Rule Enforcement
            for (rule in rules) {
                when (rule.type) {
                    "COLLISION_AVOIDANCE_PRECISION" -> {
                        // Khusus B2B Logistics: Mencegah robot bertabrakan di gudang sempit
                        buffer.confidence[i] *= rule.magnitude
                    }
                    "ENERGY_DRAIN" -> {
                        val speedSq = buffer.vx[i] * buffer.vx[i] + buffer.vy[i] * buffer.vy[i]
                        buffer.energy[i] -= (rule.magnitude * (1.0f + speedSq * 0.1f) * deltaTime).toInt()
                    }
                }
            }

            buffer.health[i] = buffer.health[i].coerceIn(0, 100)
            buffer.energy[i] = buffer.energy[i].coerceIn(0, 100)
            if (buffer.health[i] <= 0) buffer.active[i] = false
        }
    }

    private fun applyPhysicsByIndustry(buffer: AgentStateBuffer, i: Int, deltaTime: Float, industry: String) {
        val friction = when (industry) {
            "LOGISTICS" -> 0.40f // Gesekan tinggi lantai gudang
            "TRAFFIC" -> 0.10f   // Aspal mulus
            "MILITARY" -> {
                when (buffer.domain[i]) {
                    0 -> 0.02f // Air
                    1 -> 0.15f // Land
                    else -> 0.1f
                }
            }
            else -> 0.1f
        }
        buffer.vx[i] *= (1.0f - friction * deltaTime)
        buffer.vy[i] *= (1.0f - friction * deltaTime)
    }
}
