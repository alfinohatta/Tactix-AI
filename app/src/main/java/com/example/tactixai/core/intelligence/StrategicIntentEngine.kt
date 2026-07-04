package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*

/**
 * Otak di balik Emergent Warfare (Layer 6).
 * Mengelola urutan manuver kompleks seperti 'Fake Attack' (Decoy).
 */
class StrategicIntentEngine(
    private val explanationSystem: ExplanationSystem
) {
    enum class ManeuverStage { NONE, BAITING, STRIKING, WITHDRAWING }
    
    private var currentManeuver = ManeuverStage.NONE

    /**
     * Mengeksekusi manuver 'Fake Attack' sesuai blueprint.
     */
    fun executeFakeAttack(squads: List<Squad>) {
        if (squads.size < 2) return
        
        currentManeuver = ManeuverStage.BAITING
        
        // Squad 1: Menjadi Umpan (Bait) - Bergerak mencolok ke depan
        squads[0].apply {
            currentObjective = "DECOY_EXPOSURE"
            formationId = FormationType.FLYING_V.ordinal.toLong() // Formasi lebar & terlihat
        }

        // Squad 2: Penyerang Nyata (Real Strike) - Menunggu/Flanking
        squads[1].apply {
            currentObjective = "STEALTH_FLANK"
            formationId = FormationType.NEEDLE.ordinal.toLong() // Penetrasi tipis
        }

        explanationSystem.logDecision(
            "Strategic HQ",
            "EMERGENT_MANEUVER",
            "Executing 'Fake Attack' protocol. Squad 1 drawing enemy focus.",
            0.92f,
            "Success Probability: +28% through distraction."
        )
    }
}
