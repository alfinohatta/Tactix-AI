package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*

/**
 * Menerjemahkan input manusia tingkat tinggi menjadi target operasional.
 */
class CommandCenter(
    private val formationEngine: FormationEngine,
    private val explanationSystem: ExplanationSystem
) {

    enum class StrategicIntent {
        CREATE_PRESSURE, PROTECT_OBJECTIVE, GATHER_INFORMATION, TOTAL_DOMINATION
    }

    fun issueCommand(
        intent: StrategicIntent,
        commanders: List<Commander>,
        targetZone: Pair<Float, Float>
    ) {
        explanationSystem.logDecision(
            "Global Command",
            intent.name,
            "User requested strategic shift to $intent",
            1.0f,
            "Redeploying ${commanders.size} commanders to target zone"
        )

        commanders.forEach { commander ->
            when (intent) {
                StrategicIntent.CREATE_PRESSURE -> {
                    // Berikan perintah agresif ke komandan
                    // Di dunia nyata, ini akan mengubah state 'Doctrine' di CommanderAI
                }
                StrategicIntent.PROTECT_OBJECTIVE -> {
                    // Berikan koordinat pertahanan
                }
                else -> {}
            }
        }
    }
}
