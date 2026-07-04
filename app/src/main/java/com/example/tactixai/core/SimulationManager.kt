package com.example.tactixai.core

import com.example.tactixai.core.engine.*
import com.example.tactixai.core.model.* // Menggunakan model blueprint terkonsolidasi
import com.example.tactixai.core.intelligence.*
import com.example.tactixai.core.analytics.*
import com.example.tactixai.data.repository.TactixRepository
import com.example.tactixai.data.local.entities.SimulationResultEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Tactix AI Simulation Operating System (SimOS) Kernel.
 * Menghubungkan input Blueprint ke ekosistem 10.000+ agen otonom.
 */
class SimulationManager(
    val blueprint: SimulationBlueprint,
    val initialAgents: List<Agent>,
    val commanders: List<Commander>,
    val squads: List<Squad>,
    private val repository: TactixRepository,
    val userPlan: String = "FREE"
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val analytics = AnalyticsEngine()
    private val explanationSystem = ExplanationSystem()
    
    private val simEngine = SimulationEngine(
        blueprint = blueprint,
        commanders = commanders,
        initialAgents = initialAgents,
        squads = squads,
        industryType = blueprint.mapTemplate
    )

    private val outcomeAnalysis = OutcomeAnalysisEngine()

    val agents: StateFlow<List<Agent>> = simEngine.agents
    val objectiveProgress = simEngine.objectiveProgress

    /**
     * Booting ekosistem agen otonom.
     */
    fun boot() {
        val limit = if (userPlan == "ENTERPRISE") 100000 else 1000
        if (initialAgents.size > limit) {
            explanationSystem.logDecision("OS", "HALT", "Agent limit exceeded", 1.0f, "Upgrade needed")
            return
        }
        simEngine.start()
    }

    /**
     * Mematikan sistem dan sinkronisasi otomatis hasil ke Cloud MySQL.
     */
    fun shutdown() {
        simEngine.stop() // Perbaikan: Menggunakan stop() sesuai kontrak SimulationEngine terbaru
        val outcome = outcomeAnalysis.analyze(initialAgents.size, agents.value)
        
        scope.launch {
            val resultEntity = SimulationResultEntity(
                simulationId = 0, // Assigned by server
                successRate = outcome.successRate,
                efficiencyScore = outcome.efficiencyScore,
                survivalRate = outcome.survivalRate,
                resourceUsageJson = outcome.resourceUsageJson,
                aiSummary = outcome.aiSummary
            )
            repository.syncOutcomeToMySQL(resultEntity)
        }
    }

    fun getStrategicLogs() = explanationSystem.getLatestExplanations()
    fun getInfluenceMap() = simEngine.getInfluenceMap()
    fun getEconomyStatus() = simEngine.getEconomyStatus()
    fun getReplay() = simEngine.getReplay()
    fun getPerformanceReport() = analytics.calculateMetrics(agents.value)
    fun getBuffer() = simEngine.buffer
}
