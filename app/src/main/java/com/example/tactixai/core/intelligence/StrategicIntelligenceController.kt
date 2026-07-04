package com.example.tactixai.core.intelligence

import com.example.tactixai.core.engine.AgentStateBuffer
import com.example.tactixai.core.model.FormationType
import com.example.tactixai.core.model.Squad

/**
 * Otak di balik 'Emergent Intelligence'.
 * Memantau ribuan agen dan mengubah strategi secara dinamis.
 */
class StrategicIntelligenceController(
    private val explanationSystem: ExplanationSystem
) {
    /**
     * Mengevaluasi status squad secara massal dan menentukan transisi formasi.
     * Sesuai Blueprint: switching formations intelligently.
     */
    fun evaluateSquadState(
        squad: Squad,
        buffer: AgentStateBuffer,
        influenceMap: InfluenceMap
    ): FormationType {
        val squadSize = squad.memberAgentIds.size
        if (squadSize == 0) return FormationType.ADAPTIVE

        // 1. Ambil data kognitif rata-rata squad dari primitive buffer (DOD)
        var totalConfidence = 0f
        var maxThreat = 0
        var avgX = 0f; var avgY = 0f
        
        squad.memberAgentIds.forEach { id ->
            val idx = id.toInt() // Asumsi ID memetakan ke index buffer
            totalConfidence += buffer.confidence[idx]
            if (buffer.threatLevel[idx] > maxThreat) maxThreat = buffer.threatLevel[idx]
            avgX += buffer.x[idx]
            avgY += buffer.y[idx]
        }
        
        val squadConfidence = totalConfidence / squadSize
        val localInfluence = influenceMap.getInfluenceAt(avgX / squadSize, avgY / squadSize)

        // 2. Logic Transisi Cerdas (100 Formation Support)
        return when {
            // Jika dikepung (Negative Influence) & Confidence Rendah -> Turtle / Shield Wall
            localInfluence < -0.6f && squadConfidence < 0.5f -> {
                logShift(squad.name, "CRITICAL_DEFENSE", "Turtle formation activated due to high enemy dominance.")
                FormationType.TURTLE
            }
            
            // Jika terdeteksi celah (Weak point) -> Spearhead / Needle
            localInfluence > 0.1f && maxThreat < 2 -> {
                logShift(squad.name, "PENETRATION", "Detected opening. Switching to Spearhead for rapid breakthrough.")
                FormationType.SPEARHEAD
            }

            // Jika dalam mode pengintaian & visibilitas rendah -> Eagle / Ghost
            squad.currentObjective == "RECON" -> {
                FormationType.EAGLE
            }

            // Default: Grid efisien
            else -> FormationType.ADAPTIVE
        }
    }

    private fun logShift(squad: String, action: String, reason: String) {
        explanationSystem.logDecision(squad, action, reason, 0.95f, "Operational state updated.")
    }
}
