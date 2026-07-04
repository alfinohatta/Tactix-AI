package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*

/**
 * Level 2 AI: Squad Layer.
 * Mengelola koordinasi antar agen dalam satu grup kecil.
 * Bertanggung jawab atas 'Emergent Behavior' tingkat lokal.
 */
class SquadAI(
    private val squad: Squad,
    private val formationEngine: FormationEngine
) {
    private var targetPosition: Pair<Float, Float>? = null

    /**
     * Menerima perintah dari Commander dan mendistribusikannya ke Agen.
     */
    fun processDirective(directive: String, globalTarget: Pair<Float, Float>) {
        this.targetPosition = globalTarget
        squad.currentObjective = directive
        
        // Logika Emergent: Jika sedang 'ATTACK' tapi kesehatan rendah, otomatis berubah ke 'DEFENSIVE'
        // (Contoh sederhana dari Emergent Strategy)
    }

    /**
     * Menghitung target posisi individual untuk setiap agen dalam squad berdasarkan formasi.
     */
    fun getAgentTargets(agents: List<Agent>): Map<Long, Pair<Float, Float>> {
        val targets = mutableMapOf<Long, Pair<Float, Float>>()
        val center = targetPosition ?: return targets
        
        val formationType = squad.formationId?.let { FormationType.AERIAL_SPEAR } ?: FormationType.TURTLE
        
        squad.memberAgentIds.forEachIndexed { index, agentId ->
            val relative = formationEngine.getRelativePosition(
                formationType, 
                index, 
                squad.memberAgentIds.size
            )
            targets[agentId] = Pair(center.first + relative.first, center.second + relative.second)
        }
        
        return targets
    }
}
