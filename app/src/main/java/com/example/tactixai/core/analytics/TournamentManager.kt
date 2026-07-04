package com.example.tactixai.core.analytics

import com.example.tactixai.core.model.Commander
import com.example.tactixai.core.model.SimulationBlueprint
import kotlinx.coroutines.coroutineScope

/**
 * Layer 12: AI Tournament Orchestrator.
 * Menjalankan kompetisi antar AI Commander secara otonom.
 * Versi Final: Menggunakan model Blueprint terkonsolidasi.
 */
class TournamentManager(
    private val researchLab: AIResearchLab
) {
    data class TournamentResult(
        val winnerId: Long,
        val winMargin: Float,
        val battleLogs: List<String>
    )

    /**
     * Menjalankan seri pertempuran antar dua doktrin komandan.
     */
    suspend fun runMatch(
        blueprint: SimulationBlueprint,
        commanderA: Commander,
        commanderB: Commander,
        rounds: Int = 10
    ): TournamentResult = coroutineScope {
        
        // 1. Eksekusi Batch untuk Commander A
        val reportA = researchLab.runOptimizationBatch(
            blueprint.copy(commanderPersonality = commanderA.personality), 
            rounds
        )
        
        // 2. Eksekusi Batch untuk Commander B
        val reportB = researchLab.runOptimizationBatch(
            blueprint.copy(commanderPersonality = commanderB.personality), 
            rounds
        )

        val winner = if (reportA.winRate > reportB.winRate) commanderA else commanderB
        val margin = Math.abs(reportA.winRate - reportB.winRate)

        TournamentResult(
            winnerId = winner.id,
            winMargin = margin,
            battleLogs = listOf(
                "Matchup: ${commanderA.name} vs ${commanderB.name}",
                "Doctrine A Win Rate: ${reportA.winRate}%",
                "Doctrine B Win Rate: ${reportB.winRate}%",
                "Intelligence Score A: ${reportA.intelligenceScore}",
                "Intelligence Score B: ${reportB.intelligenceScore}"
            )
        )
    }
}
