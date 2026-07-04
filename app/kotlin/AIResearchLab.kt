package com.example.tactixai.core.analytics

import com.example.tactixai.core.SimulationBlueprint
import com.example.tactixai.core.engine.HeadlessSimulationEngine
import com.example.tactixai.core.model.FormationType
import com.example.tactixai.core.intelligence.StrategyLearningEngine
import kotlinx.coroutines.*

/**
 * Layer 11: AI Research Lab Mode.
 * Mengubah platform menjadi mesin eksperimen massal (Monte-Carlo).
 * Menjalankan ribuan simulasi secepat mungkin untuk menemukan strategi otonom optimal.
 * Mendukung Reinforcement Learning Feedback Loop.
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
        val intelligenceScore: Int,      // Diambil dari Reinforcement Learning Reward
        val detectedBottlenecks: List<String>,
        val totalAgentsSimulated: Int,
        val executionTimeMs: Long
    )

    /**
     * Menjalankan Batch Simulation sesuai Blueprint Poin 11.
     * Menggunakan Coroutines untuk paralelisme maksimal di Multi-core Android.
     */
    suspend fun runOptimizationBatch(
        blueprint: SimulationBlueprint,
        iterations: Int = 1000
    ): LabReport = coroutineScope {
        val startTime = System.currentTimeMillis()
        
        // 1. Jalankan simulasi secara paralel (Monte-Carlo Approach)
        val results = (1..iterations).map {
            async { 
                val outcome = headlessEngine.runBatch(blueprint)
                
                // --- Feedback Loop ke Layer 5: Reinforcement Learning ---
                // Mencatat hasil untuk evolusi strategi AI di simulasi mendatang
                learningEngine.recordOutcome(
                    scenarioType = blueprint.mapTemplate,
                    formationUsed = blueprint.initialFormation,
                    survivalRate = outcome.survivalRate,
                    energyEfficiency = outcome.efficiencyScore
                )
                outcome
            }
        }.awaitAll()

        // 2. Analisis Hasil (Analytics Engine Layer)
        val winCount = results.count { it.survivalRate > 0.6f }
        val avgSurvival = results.map { it.survivalRate }.average().toFloat()
        val avgEfficiency = results.map { it.efficiencyScore }.average().toFloat()
        
        // 3. Deteksi pola kegagalan sistemik (Strategic Bottleneck Analysis)
        val bottlenecks = performBottleneckAnalysis(results)

        LabReport(
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

    /**
     * Strategic Bottleneck Analysis (Layer 11 Specs).
     * Menganalisis data statistik dari 1000 simulasi untuk mencari titik lemah sistemik.
     */
    private fun performBottleneckAnalysis(results: List<AnalyticsEngine.SimulationMetrics>): List<String> {
        val analysis = mutableListOf<String>()
        
        // Cek Bottleneck Energi
        val lowEnergySims = results.count { it.totalEnergyConsumed > 800f }
        if (lowEnergySims > results.size / 2) {
            analysis.add("CRITICAL: Strategic Energy Scarcity detected in 50%+ runs.")
        }

        // Cek Kerapuhan Formasi (Attrition Rate)
        val highAttritionSims = results.count { it.survivalRate < 0.2f }
        if (highAttritionSims > results.size / 4) {
            analysis.add("WARNING: High Attrition Rate. Defensive layers insufficient for this doctrine.")
        }

        // Cek Latensi Logika (Android Optimization Awareness)
        val avgComputeTime = results.map { it.efficiencyScore }.average() 
        if (avgComputeTime < 0.3f) {
            analysis.add("PERFORMANCE: Logic latency exceeding target. Optimize Agent Brain hierarchy.")
        }
        
        return analysis
    }
}
