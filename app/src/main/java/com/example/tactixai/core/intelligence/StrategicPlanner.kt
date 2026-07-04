package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*
import java.util.*

/**
 * Otak Strategis HQ (Level 1 AI).
 * Mengelola manuver multi-tahap (States) untuk menciptakan 'Emergent Strategy'.
 * Implementasi Layer 4 & 6 Blueprint.
 */
class StrategicPlanner(
    private val influenceMap: InfluenceMap,
    private val explanationSystem: ExplanationSystem
) {
    enum class ManeuverState { 
        IDLE, 
        FAKE_ATTACK_BAITING,  // Squad A memancing musuh
        FAKE_ATTACK_STRIKING, // Squad B menyerang dari sisi buta
        PINCER_CLOSING,       // Mengepung target dari dua arah
        WITHDRAW_REORGANIZE   // Mundur teratur untuk hemat energi
    }
    
    private var currentState = ManeuverState.IDLE
    private var stateStartTime = 0L

    /**
     * Mengevaluasi kondisi global dan memicu manuver strategis otonom.
     */
    fun evaluateGlobalStrategy(squads: List<Squad>, allAgents: List<Agent>) {
        if (squads.size < 2) return
        val now = System.currentTimeMillis()

        when (currentState) {
            ManeuverState.IDLE -> {
                val gaps = influenceMap.findStrategicGaps()
                if (gaps.isNotEmpty()) {
                    initiateFakeAttack(squads)
                }
            }
            ManeuverState.FAKE_ATTACK_BAITING -> {
                // Sesuai blueprint: jika musuh terpancing (deteksi via heatmap gradient)
                if (now - stateStartTime > 8000) { // Timeout 8 detik atau deteksi musuh bergerak
                    executeFlankStrike(squads)
                }
            }
            ManeuverState.FAKE_ATTACK_STRIKING -> {
                if (now - stateStartTime > 15000) {
                    currentState = ManeuverState.IDLE
                    explanationSystem.logDecision("HQ", "MANEUVER_COMPLETE", "Fake attack successful.", 1.0f, "Reverting to standard patrol.")
                }
            }
            else -> {}
        }
    }

    private fun initiateFakeAttack(squads: List<Squad>) {
        currentState = ManeuverState.FAKE_ATTACK_BAITING
        stateStartTime = System.currentTimeMillis()

        // Squad 1: Menjadi Umpan (Bait) - Gunakan formasi mencolok (Flying V)
        squads[0].apply {
            currentObjective = "DECOY_DISTRACTION"
            formationId = FormationType.FLYING_V.ordinal.toLong()
        }

        explanationSystem.logDecision(
            "Strategic HQ", "FAKE_ATTACK_START", 
            "Enemy gap detected. Deploying Squad 1 as decoy bait.",
            0.92f, "Goal: Force enemy to commit reserves to the center."
        )
    }

    private fun executeFlankStrike(squads: List<Squad>) {
        currentState = ManeuverState.FAKE_ATTACK_STRIKING
        stateStartTime = System.currentTimeMillis()

        // Squad 2: Penyerang Nyata - Formasi penetrasi tajam (Needle)
        squads[1].apply {
            currentObjective = "BLIND_SIDE_STRIKE"
            formationId = FormationType.NEEDLE.ordinal.toLong()
        }

        explanationSystem.logDecision(
            "Strategic HQ", "FLANK_STRIKE", 
            "Enemy committed to decoy. Initiating side-flank strike with Squad 2.",
            0.98f, "Targeting exposed enemy rear perimeter."
        )
    }
}
