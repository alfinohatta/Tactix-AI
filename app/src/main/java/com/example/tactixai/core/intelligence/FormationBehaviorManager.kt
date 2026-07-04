package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*
import com.example.tactixai.core.analytics.AnalyticsEngine

/**
 * Otak di balik transisi cerdas antar 100 formasi.
 * Implementasi 'Transition Rules' sesuai Blueprint Strategis.
 */
class FormationBehaviorManager(
    private val explanationSystem: ExplanationSystem
) {
    /**
     * Menentukan formasi terbaik berdasarkan input sensorik squad dan data intelijen makro.
     */
    fun selectOptimalFormation(
        squad: Squad,
        nearbyEnemies: List<Agent>,
        metrics: AnalyticsEngine.SimulationMetrics
    ): FormationType {
        val currentFormation = squad.formationId?.let { 
            if (it >= 0 && it < FormationType.entries.size) FormationType.entries[it.toInt()] 
            else FormationType.FANG_ZHEN 
        } ?: FormationType.FANG_ZHEN

        val enemyCount = nearbyEnemies.size
        val squadSize = squad.memberAgentIds.size
        val threatRatio = if (squadSize > 0) enemyCount.toFloat() / squadSize else 1.0f

        // IMPLEMENTASI TRANSITION RULES (Blueprint Strategic Note)
        return when {
            // Rule 1: Scout detected enemy -> Eagle Formation (Observation)
            nearbyEnemies.isNotEmpty() && threatRatio < 0.2f -> {
                if (currentFormation != FormationType.EAGLE) {
                    logTransition(squad.name, currentFormation, FormationType.EAGLE, "Enemy scouted at distance. Optimizing field of view.")
                }
                FormationType.EAGLE
            }

            // Rule 2: Heavy Attack -> Shield Wall / Ring (Defense)
            threatRatio > 0.8f -> {
                if (currentFormation != FormationType.SHIELD_WALL && currentFormation != FormationType.TURTLE) {
                    logTransition(squad.name, currentFormation, FormationType.FORTRESS_RING, "High threat density ($threatRatio). Activating perimeter defense.")
                }
                FormationType.FORTRESS_RING
            }

            // Rule 3: Enemy weak point found -> Spearhead / Hammer (Offensive)
            threatRatio in 0.2f..0.5f && metrics.efficiencyScore > 0.6f -> {
                if (currentFormation != FormationType.SPEARHEAD) {
                    logTransition(squad.name, currentFormation, FormationType.SPEARHEAD, "Strategic gap identified. Initiating penetration strike.")
                }
                FormationType.SPEARHEAD
            }

            // Layer 3 Blueprint Expansion: Dynamic Transition
            // Rule 4: Flanking Opportunity -> Wedge / Pincer
            threatRatio in 0.1f..0.4f && metrics.efficiencyScore < 0.5f -> {
                if (currentFormation != FormationType.WEDGE) {
                    logTransition(squad.name, currentFormation, FormationType.WEDGE, "Executing flanking maneuver to bypass defense.")
                }
                FormationType.WEDGE
            }

            // Rule 5: Enemy retreating -> Wolf Pack (Pursuit)
            nearbyEnemies.any { it.status == AgentStatus.RETREATING } -> {
                if (currentFormation != FormationType.WOLF_PACK) {
                    logTransition(squad.name, currentFormation, FormationType.WOLF_PACK, "Target in retreat. Transitioning to pack pursuit.")
                }
                FormationType.WOLF_PACK
            }

            // Rule 6: High Confidence + Low Visibility -> Silent Arrow (Stealth Attack)
            threatRatio < 0.2f && metrics.survivalRate > 0.8f -> {
                if (currentFormation != FormationType.SILENT_ARROW) {
                    logTransition(squad.name, currentFormation, FormationType.SILENT_ARROW, "Optimal conditions for covert engagement.")
                }
                FormationType.SILENT_ARROW
            }

            // Rule 7: Extreme Casualties -> Turtle (Maximum Defense)
            metrics.survivalRate < 0.3f -> {
                if (currentFormation != FormationType.TURTLE) {
                    logTransition(squad.name, currentFormation, FormationType.TURTLE, "Critical losses. Prioritizing individual survival.")
                }
                FormationType.TURTLE
            }

            // Blueprint Layer 3: Dynamic Formation
            // Rule 8: High Value Unit Detected -> Orbit (Protection)
            nearbyEnemies.any { it.role == AgentRole.COMMANDER } && threatRatio < 0.5f -> {
                if (currentFormation != FormationType.ORBIT) {
                    logTransition(squad.name, currentFormation, FormationType.ORBIT, "VIP Target/Protection priority triggered.")
                }
                FormationType.ORBIT
            }

            else -> currentFormation
        }
    }

    private fun logTransition(squadName: String, from: FormationType, to: FormationType, reason: String) {
        explanationSystem.logDecision(
            squadName,
            "FORMATION_SHIFT",
            "From $from to $to. Reasoning: $reason",
            0.95f,
            "Tactical state synchronized."
        )
    }
}
