package com.example.tactixai.core.analytics

import com.example.tactixai.core.model.SimulationBlueprint
import com.example.tactixai.core.engine.HeadlessSimulationEngine
import com.example.tactixai.core.model.FormationType
import com.example.tactixai.core.intelligence.StrategyLearningEngine
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Layer 11: AI Research Lab Mode.
 * Menjalankan simulasi batch (Monte-Carlo) untuk memvalidasi strategi secara statistik.
 */
class AIResearchLab(
    private val headlessEngine: HeadlessSimulationEngine,
    private val learningEngine: StrategyLearningEngine
) {

    data class LabReport(
        val testedFormation: FormationType,
        val winRate: Float,
        val avgSurvivalRate: Float,
        val avgEfficiency: Float,
        val intelligenceScore: Int,
        val detectedBottlenecks: List<String>,
        val totalAgentsSimulated: Int,
        val executionTimeMs: Long
    )

    /**
     * Menjalankan Batch Simulation sesuai Blueprint Poin 11.
     */
    suspend fun runOptimizationBatch(
        blueprint: SimulationBlueprint,
        iterations: Int = 1000
    ): LabReport = coroutineScope {
        val startTime = System.currentTimeMillis()
        
        val results = (1..iterations).map {
            async { 
                val outcome = headlessEngine.runBatch(blueprint)
                
                // Feedback Loop ke Layer 5: Reinforcement Learning
                // Fix: Mengirim FormationType langsung (bukan name string)
                learningEngine.recordOutcome(
                    scenarioType = blueprint.mapTemplate,
                    formationUsed = blueprint.initialFormation,
                    survivalRate = outcome.survivalRate,
                    energyEfficiency = outcome.efficiencyScore
                )
                outcome
            }
        }.awaitAll()

        val winCount = results.count { it.survivalRate > 0.6f }
        val avgSurvival = results.map { it.survivalRate }.average().toFloat()
        val avgEfficiency = results.map { it.efficiencyScore }.average().toFloat()
        
        val bottlenecks = performBottleneckAnalysis(results)

        return@coroutineScope LabReport(
            testedFormation = blueprint.initialFormation,
            winRate = (winCount.toFloat() / iterations) * 100f,
            avgSurvivalRate = avgSurvival * 100f,
            avgEfficiency = avgEfficiency,
            intelligenceScore = learningEngine.getConfidenceScore(blueprint.mapTemplate, blueprint.initialFormation),
            detectedBottlenecks = bottlenecks,
            totalAgentsSimulated = blueprint.agentCount * iterations,
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }

    private fun performBottleneckAnalysis(results: List<AnalyticsEngine.SimulationMetrics>): List<String> {
        val analysis = mutableListOf<String>()
        val lowEnergySims = results.count { it.totalEnergyConsumed > 800f }
        if (lowEnergySims > results.size / 2) {
            analysis.add("CRITICAL: Strategic Energy Scarcity detected.")
        }
        return analysis
    }
}
