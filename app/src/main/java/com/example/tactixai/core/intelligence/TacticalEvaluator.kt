package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.Squad
import com.example.tactixai.core.model.SquadStatus

/**
 * Otak Taktis Lapangan.
 * Menilai situasi lokal dan memicu 'Emergent Strategy'.
 */
class TacticalEvaluator(
    private val influenceMap: InfluenceMap,
    private val explanationSystem: ExplanationSystem
) {
    /**
     * Mengevaluasi kondisi squad dan menyarankan aksi taktis mendesak.
     */
    fun evaluateSquadTactics(squad: Squad, squadAgents: List<Agent>): TacticSuggestion {
        val avgX = squadAgents.map { it.x }.average().toFloat()
        val avgY = squadAgents.map { it.y }.average().toFloat()
        
        // Baca pengaruh di posisi squad
        val currentInfluence = influenceMap.getInfluenceAt(avgX, avgY)
        
        // 1. Logic: Tactical Retreat (Jika pengaruh musuh terlalu dominan)
        if (currentInfluence < -0.8f) {
            explanationSystem.logDecision(
                squad.name,
                "TACTICAL_RETREAT",
                "Influence dropped to $currentInfluence. Overwhelmed by enemy presence.",
                0.9f,
                "Repositioning to safe zone."
            )
            return TacticSuggestion.RETREAT
        }

        // 2. Logic: Flanking Opportunity (Cari area sekitar dengan pengaruh musuh rendah)
        val flankPoint = findFlankingOpportunity(avgX, avgY)
        if (flankPoint != null) {
            explanationSystem.logDecision(
                squad.name,
                "FLANKING_MANEUVER",
                "Detected weak point in enemy perimeter.",
                0.75f,
                "Executing flanking maneuver to maximize damage."
            )
            return TacticSuggestion.FLANK(flankPoint.first, flankPoint.second)
        }

        return TacticSuggestion.MAINTAIN
    }

    private fun findFlankingOpportunity(x: Float, y: Float): Pair<Float, Float>? {
        // Scan area sekitar untuk mencari influence musuh yang lemah (-0.2 sampai 0.2)
        // Implementasi sederhana: cek 4 arah
        val offsets = listOf(Pair(100f, 0f), Pair(-100f, 0f), Pair(0f, 100f), Pair(0f, -100f))
        for (off in offsets) {
            val checkX = x + off.first
            val checkY = y + off.second
            val inf = influenceMap.getInfluenceAt(checkX, checkY)
            if (inf in -0.2f..0.2f) return Pair(checkX, checkY)
        }
        return null
    }

    sealed class TacticSuggestion {
        object MAINTAIN : TacticSuggestion()
        object RETREAT : TacticSuggestion()
        data class FLANK(val targetX: Float, val targetY: Float) : TacticSuggestion()
    }
}
