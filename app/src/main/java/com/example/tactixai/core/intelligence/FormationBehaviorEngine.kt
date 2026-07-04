package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*
import com.example.tactixai.core.analytics.AnalyticsEngine

/**
 * Layer 3 & 4: Formation & Strategy Engine.
 * Otak di balik transisi cerdas antar 100 formasi SimOS.
 */
class FormationBehaviorEngine(
    private val explanationSystem: ExplanationSystem
) {
    /**
     * Menentukan formasi terbaik berdasarkan input sensorik dan intelijen.
     */
    fun selectOptimalFormation(
        squad: Squad,
        nearbyEnemies: List<Agent>,
        metrics: AnalyticsEngine.SimulationMetrics
    ): FormationType {
        val threatLevel = calculateThreatLevel(squad, nearbyEnemies)
        
        // Default fallback
        val currentFormation = try {
            squad.formationId?.let { FormationType.entries[it.toInt()] } ?: FormationType.FANG_ZHEN
        } catch (e: Exception) {
            FormationType.FANG_ZHEN
        }

        return when {
            // Rule: Scout detected enemy -> EAGLE
            nearbyEnemies.isNotEmpty() && threatLevel < 0.2f -> {
                if (currentFormation != FormationType.EAGLE) {
                    logTransition(squad.name, currentFormation, FormationType.EAGLE, "Enemy detected. Optimizing vision.")
                }
                FormationType.EAGLE
            }

            // Rule: Critical Threat -> FORTRESS_RING
            threatLevel > 0.7f -> {
                if (currentFormation != FormationType.FORTRESS_RING) {
                    logTransition(squad.name, currentFormation, FormationType.FORTRESS_RING, "Heavy threat. Max perimeter defense.")
                }
                FormationType.FORTRESS_RING
            }

            // Rule: Numerical Advantage -> PINCER (Executing Encirclement)
            threatLevel in 0.3f..0.6f && squad.memberAgentIds.size > nearbyEnemies.size -> {
                if (currentFormation != FormationType.PINCER) {
                    logTransition(squad.name, currentFormation, FormationType.PINCER, "Advantage found. Surrounding target.")
                }
                FormationType.PINCER
            }

            // Layer 3 Blueprint: Dynamic Formation Generation
            // Rule: Offensive Thrust -> SPEARHEAD
            threatLevel < 0.4f && metrics.efficiencyScore > 0.8f -> {
                if (currentFormation != FormationType.SPEARHEAD) {
                    logTransition(squad.name, currentFormation, FormationType.SPEARHEAD, "Optimal state for penetration.")
                }
                FormationType.SPEARHEAD
            }

            // Rule: Stealth Infiltration -> GHOST
            metrics.survivalRate < 0.5f && threatLevel < 0.3f -> {
                if (currentFormation != FormationType.GHOST) {
                    logTransition(squad.name, currentFormation, FormationType.GHOST, "Low survival. Masking presence.")
                }
                FormationType.GHOST
            }

            else -> currentFormation
        }
    }

    private fun calculateThreatLevel(squad: Squad, enemies: List<Agent>): Float {
        if (squad.memberAgentIds.isEmpty()) return 1.0f
        return enemies.size.toFloat() / squad.memberAgentIds.size.toFloat()
    }

    private fun logTransition(squadName: String, from: FormationType, to: FormationType, reason: String) {
        explanationSystem.logDecision(
            squadName,
            "FORMATION_SHIFT",
            "Transitioned from $from to $to. Reasoning: $reason",
            0.95f,
            "Tactical adaptation."
        )
    }
}
