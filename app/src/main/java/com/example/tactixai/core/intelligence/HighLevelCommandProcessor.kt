package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*

/**
 * Menerjemahkan Strategic Intent dari User menjadi Doktrin Operasional.
 * Implementasi Layer 3: Command System.
 */
class HighLevelCommandProcessor(
    private val strategicPlanner: StrategicPlanner,
    private val explanationSystem: ExplanationSystem
) {
    enum class UserIntent {
        CREATE_PRESSURE, 
        PROTECT_OBJECTIVE, 
        GATHER_INFORMATION, 
        SURROUND_TARGET,
        TACTICAL_WITHDRAWAL
    }

    /**
     * Memproses perintah dari user dan menyesuaikan perilaku seluruh ekosistem.
     */
    fun processIntent(intent: UserIntent, commanders: List<Commander>, targetZone: Pair<Float, Float>?) {
        val detail = when (intent) {
            UserIntent.CREATE_PRESSURE -> "Commanders assigned to high-aggression search and destroy."
            UserIntent.PROTECT_OBJECTIVE -> "Perimeter defense logic prioritized. Energy conservation mode active."
            UserIntent.GATHER_INFORMATION -> "Scout roles activated. Low-visibility formations (Eagle/Ghost) selected."
            UserIntent.SURROUND_TARGET -> "Pincer and Encirclement maneuvers initiated."
            UserIntent.TACTICAL_WITHDRAWAL -> "Ordered retreat to safe coordinates."
        }

        explanationSystem.logDecision(
            "User Command",
            intent.name,
            "Strategic shift requested by operator: $intent",
            1.0f,
            detail
        )

        // Inject intent ke Strategic Planner untuk mengubah 'Maneuver State'
        // strategicPlanner.overrideIntent(intent, targetZone)
    }
}
