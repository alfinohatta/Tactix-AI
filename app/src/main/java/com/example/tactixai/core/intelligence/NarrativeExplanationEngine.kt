package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.FormationType
import com.example.tactixai.core.model.Squad

/**
 * Layer 9: AI Explanation System.
 * Mengubah data metrik mentah menjadi narasi strategis untuk user.
 */
class NarrativeExplanationEngine {

    fun explainFormationChange(
        squad: Squad,
        old: FormationType,
        new: FormationType,
        reason: String,
        survivalProbability: Float
    ): String {
        return """
            [TACTICAL LOG] ${squad.name} changed formation.
            TRANSITION: $old -> $new
            REASON: $reason
            PREDICTED SURVIVAL: ${(survivalProbability * 100).toInt()}%
        """.trimIndent()
    }

    fun explainStrategicIntent(maneuver: String, impact: String): String {
        return "COMMANDER INTENT: $maneuver. EXPECTED IMPACT: $impact."
    }
}
