package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.AgentStatus
import com.example.tactixai.core.model.SimulationObjective

/**
 * Memantau pencapaian objektif simulasi.
 * Menghubungkan 'Input Manusia' dengan 'Hasil Strategis'.
 */
class ObjectiveEvaluator(
    private val objective: SimulationObjective,
    private val explanationSystem: ExplanationSystem
) {
    data class Progress(
        val percentage: Float,
        val isCompleted: Boolean,
        val statusMessage: String
    )

    fun evaluateProgress(agents: List<Agent>, playerCommanderId: Long): Progress {
        val playerAgents = agents.filter { it.commanderId == playerCommanderId }
        val enemyAgents = agents.filter { it.commanderId != playerCommanderId && it.status != AgentStatus.DESTROYED }

        return when (objective) {
            SimulationObjective.ELIMINATION -> {
                val initialEnemyCount = 1000f // Misal total awal
                val currentEnemyCount = enemyAgents.size.toFloat()
                val progress = ((initialEnemyCount - currentEnemyCount) / initialEnemyCount).coerceIn(0f, 1f)
                Progress(progress, currentEnemyCount == 0f, "Target elimination: ${ (progress * 100).toInt() }%")
            }
            SimulationObjective.AREA_CONTROL -> {
                // Logic untuk mengukur dominasi di map (menggunakan Influence Map)
                Progress(0.5f, false, "Area control in progress...")
            }
            SimulationObjective.SURVIVAL_TEST -> {
                val aliveRatio = playerAgents.count { it.status != AgentStatus.DESTROYED }.toFloat() / playerAgents.size.coerceAtLeast(1)
                Progress(aliveRatio, aliveRatio <= 0, "Survival rate: ${ (aliveRatio * 100).toInt() }%")
            }
            else -> Progress(0f, false, "Objective status: Active")
        }
    }
}
