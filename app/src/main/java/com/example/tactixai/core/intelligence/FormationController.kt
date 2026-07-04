package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*

/**
 * Otak di balik Dynamic Formation Generation.
 * Mengubah formasi berdasarkan ancaman (Threat) dan lingkungan secara real-time.
 */
class FormationController(
    private val explanationSystem: ExplanationSystem
) {
    /**
     * Mengevaluasi apakah sebuah Squad perlu berganti formasi.
     * Implementasi: V -> Crescent (saat dikepung) -> Ring (saat terdesak).
     */
    fun evaluateFormation(
        squad: Squad,
        agents: List<Agent>,
        nearbyEnemies: List<Agent>
    ): FormationType {
        val currentFormation = squad.formationId?.let { FormationType.AERIAL_SPEAR } ?: FormationType.TURTLE
        
        // 1. Hitung arah ancaman
        if (nearbyEnemies.isEmpty()) return FormationType.AERIAL_SPEAR // Default Spearhead

        val threatLevel = nearbyEnemies.size.toFloat() / squad.memberAgentIds.size.toFloat()
        
        // 2. Logic: Crescent (Bulan Sabit) jika musuh terkonsentrasi di depan
        if (threatLevel > 0.3f && threatLevel <= 0.7f) {
            if (currentFormation != FormationType.PINCER) {
                explanationSystem.logDecision(
                    squad.name,
                    "FORMATION_CHANGE",
                    "Threat detected at 40%. Adapting to Crescent/Pincer for better coverage.",
                    0.85f,
                    "Increased front-line fire width."
                )
                return FormationType.PINCER
            }
        }

        // 3. Logic: Defensive Ring jika dikepung (Surrounded)
        if (threatLevel > 0.7f) {
            if (currentFormation != FormationType.RING) {
                explanationSystem.logDecision(
                    squad.name,
                    "FORMATION_CHANGE",
                    "CRITICAL: Surrounded by enemies. Switching to Defensive Ring.",
                    0.98f,
                    "Maximum perimeter defense activated."
                )
                return FormationType.RING
            }
        }

        return currentFormation
    }
}
