package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*

/**
 * Otak di balik 'Formation Behaviors + Transition Rules'.
 * Mengimplementasikan logika cerdas untuk perpindahan antar 100 formasi.
 */
class TacticalStateEvaluator(private val explanationSystem: ExplanationSystem) {

    /**
     * Mengevaluasi kondisi lingkungan dan menentukan formasi optimal.
     */
    fun evaluateTransition(
        squad: Squad,
        nearbyEnemies: Int,
        threatLevel: Float,
        isEnemyRetreating: Boolean
    ): FormationType {
        val current = squad.formationId?.let { FormationType.entries[it.toInt()] } ?: FormationType.FANG_ZHEN

        return when {
            // Rule 1: Scout detected enemy (Low Threat) -> Eagle Formation (Observation)
            nearbyEnemies > 0 && threatLevel < 0.2f -> {
                if (current != FormationType.EAGLE) log("Eagle", "Enemy scouted. Expanding vision radius.")
                FormationType.EAGLE
            }

            // Rule 2: Under Heavy Fire (High Threat) -> Shield Wall / Ring
            threatLevel > 0.7f -> {
                if (current != FormationType.SHIELD_WALL && current != FormationType.FORTRESS_RING) {
                    log("Shield Ring", "Critical threat detected. Maximizing perimeter defense.")
                }
                FormationType.FORTRESS_RING
            }

            // Rule 3: Enemy Weak Point / Numerical Advantage -> Spearhead (Penetration)
            threatLevel in 0.2f..0.5f && !isEnemyRetreating -> {
                if (current != FormationType.SPEARHEAD) log("Spearhead", "Opening detected. Initiating breakthrough.")
                FormationType.SPEARHEAD
            }

            // Rule 4: Enemy Retreating -> Wolf Pack (Pursuit)
            isEnemyRetreating -> {
                if (current != FormationType.WOLF_PACK) log("Wolf Pack", "Target in retreat. Activating pursuit protocol.")
                FormationType.WOLF_PACK
            }

            else -> current
        }
    }

    private fun log(target: String, reason: String) {
        explanationSystem.logDecision("Tactical HQ", "TRANSITION", "Shift to $target", 0.95f, reason)
    }
}
