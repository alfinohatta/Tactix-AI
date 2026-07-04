package com.example.tactixai.core.engine

import com.example.tactixai.core.model.*
import com.example.tactixai.core.intelligence.*
import com.example.tactixai.core.analytics.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Ultimate SimOS Engine (v22 - Stable).
 * Menyatukan DOD Physics, Industry Rules, dan Strategic AI.
 */
class SimulationEngine(
    private val blueprint: SimulationBlueprint,
    private val commanders: List<Commander>,
    private val initialAgents: List<Agent>,
    private val squads: List<Squad>,
    private val industryType: String = "MILITARY"
) {
    // 1. Data-Oriented Storage (Technical Moat)
    // Kotlin secara otomatis men-generate getBuffer() untuk properti publik ini.
    val buffer = AgentStateBuffer(15000) 
    private val physics = MultiDomainPhysics()
    
    private val spatialGrid = SpatialGrid(blueprint.width.toFloat(), blueprint.height.toFloat(), 60f)
    private val combatEngine = CombatEngine()
    private val rulesEngine = RulesEngine(blueprint.rules)
    private val influenceMap = InfluenceMap(blueprint.width.toFloat(), blueprint.height.toFloat())
    
    // 2. Intelligence Layers
    private val explanationSystem = ExplanationSystem()
    private val formationEngine = FormationEngine()
    private val behaviorManager = FormationBehaviorManager(explanationSystem)
    private val flockingEngine = FlockingEngine()
    private val strategicPlanner = StrategicPlanner(influenceMap, explanationSystem)
    private val economyEngine = EconomyEngine()
    private val replaySystem = BattleReplaySystem()
    private val objectiveEvaluator = ObjectiveEvaluator(blueprint.objective, explanationSystem)
    private val analytics = AnalyticsEngine()

    private var isRunning = false
    private val engineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var frameCount = 0

    private val _agents = MutableStateFlow(initialAgents)
    val agents = _agents.asStateFlow()
    
    private val _objectiveProgress = MutableStateFlow(ObjectiveEvaluator.Progress(0f, false, "Ready"))
    val objectiveProgress = _objectiveProgress.asStateFlow()

    init {
        initialAgents.forEachIndexed { i, agent ->
            buffer.setAgent(
                i, agent.x, agent.y, agent.domain.ordinal, 
                agent.commanderId ?: 0L, agent.squadId ?: 0L
            )
            buffer.confidence[i] = agent.confidence
        }
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        engineScope.launch {
            var lastTime = System.currentTimeMillis()
            while (isRunning) {
                val now = System.currentTimeMillis()
                val deltaTime = (now - lastTime) / 1000f
                lastTime = now
                processSimOSCycle(deltaTime)
                frameCount++
                delay(16)
            }
        }
    }

    private suspend fun processSimOSCycle(deltaTime: Float) = withContext(Dispatchers.Default) {
        // --- LEVEL 1: Commander AI (High Frequency - Blueprint Point 7) ---
        if (frameCount % 15 == 0) {
            val modelList = syncToModelList()
            replaySystem.recordFrame(System.currentTimeMillis(), modelList, emptyList())
            influenceMap.updateFromBuffer(buffer)
            strategicPlanner.evaluateGlobalStrategy(squads, modelList)
            _objectiveProgress.value = objectiveEvaluator.evaluateProgress(modelList, commanders.firstOrNull()?.id ?: 0L)
        }

        // --- LEVEL 2: Squad AI (Medium Frequency) ---
        if (frameCount % 45 == 0) {
            val metrics = analytics.calculateMetrics(syncToModelList())
            squads.forEach { squad ->
                val neighbors = spatialGrid.getNeighborsIndices(0f, 0f, 1000f) // Simplified squad sensing
                val enemies = neighbors.filter { buffer.commanderId[it] != squad.commanderId }
                    .map { syncAgentFromBuffer(it) }
                
                val newFormation = behaviorManager.selectOptimalFormation(squad, enemies, metrics)
                squad.formationId = newFormation.ordinal.toLong()

                // UPDATE AGENT TARGETS BASED ON FORMATION
                val fType = FormationType.entries[squad.formationId?.toInt() ?: 0]
                val squadCenter = calculateSquadCenter(squad)
                
                squad.memberAgentIds.forEachIndexed { index, agentId ->
                    val idx = agentId.toInt()
                    val rel = formationEngine.getRelativePosition(fType, index, squad.memberAgentIds.size)
                    buffer.targetX[idx] = squadCenter.first + rel.first
                    buffer.targetY[idx] = squadCenter.second + rel.second
                }
            }
        }

        // --- LEVEL 3: Individual AI (Low Frequency - Cognitive Brain) ---
        if (frameCount % 90 == 0) {
            for (i in 0 until initialAgents.size) {
                if (!buffer.active[i]) continue
                
                val localInfluence = influenceMap.getInfluenceAt(buffer.x[i], buffer.y[i])
                if (localInfluence < -0.4f) {
                    buffer.confidence[i] = (buffer.confidence[i] - 0.05f).coerceAtLeast(0.1f)
                    buffer.threatLevel[i] = 2 // High
                } else {
                    buffer.confidence[i] = (buffer.confidence[i] + 0.02f).coerceAtMost(1.0f)
                    buffer.threatLevel[i] = 0 // Low
                }
            }
        }

        spatialGrid.clear()
        for (i in 0 until initialAgents.size) {
            if (buffer.active[i]) spatialGrid.insert(i, buffer.x[i], buffer.y[i])
        }

        rulesEngine.applyRulesToBuffer(buffer, deltaTime, industryType)
        economyEngine.regenerate(deltaTime)

        val numAgents = initialAgents.size
        val chunkSize = 2000
        (0 until numAgents step chunkSize).map { start ->
            async {
                combatEngine.processCombatBuffer(buffer, spatialGrid, deltaTime)
                for (i in start until minOf(start + 2000, numAgents)) {
                    if (!buffer.active[i]) continue
                    
                    // Lightweight Movement (Boids + Seeking + Physics)
                    val neighbors = spatialGrid.getNeighborsIndices(buffer.x[i], buffer.y[i], 50f)
                    var (fx, fy) = flockingEngine.calculateFlockingForce(i, buffer, neighbors)

                    // Seeking Force (Mover towards formation target)
                    if (buffer.targetX[i] != 0f) {
                        val dx = buffer.targetX[i] - buffer.x[i]
                        val dy = buffer.targetY[i] - buffer.y[i]
                        val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                        if (dist > 10f) {
                            fx += (dx / dist) * 2.0f
                            fy += (dy / dist) * 2.0f
                        }
                    }

                    val (nvx, nvy) = physics.applyPhysics(
                        AgentDomain.entries[buffer.domain[i]],
                        buffer.vx[i], buffer.vy[i], fx, fy, deltaTime
                    )
                    
                    buffer.vx[i] = nvx
                    buffer.vy[i] = nvy
                    buffer.x[i] += buffer.vx[i] * deltaTime * 100f
                    buffer.y[i] += buffer.vy[i] * deltaTime * 100f
                }
            }
        }.awaitAll()

        // Sync StateFlow hanya pada 1 FPS untuk menghemat CPU (Optimization Point 7)
        if (frameCount % 60 == 0) _agents.value = syncToModelList()
    }

    private fun syncAgentFromBuffer(i: Int): Agent {
        val agent = initialAgents[i]
        return agent.copy(
            x = buffer.x[i], y = buffer.y[i],
            health = buffer.health[i],
            confidence = buffer.confidence[i],
            squadId = if (buffer.squadId[i] != 0L) buffer.squadId[i] else null,
            status = if (!buffer.active[i]) AgentStatus.DESTROYED else agent.status
        )
    }

    private fun syncToModelList(): List<Agent> {
        return initialAgents.mapIndexed { i, agent ->
            agent.copy(
                x = buffer.x[i], y = buffer.y[i], 
                health = buffer.health[i], 
                confidence = buffer.confidence[i],
                squadId = if (buffer.squadId[i] != 0L) buffer.squadId[i] else null,
                status = if (!buffer.active[i]) AgentStatus.DESTROYED else agent.status
            )
        }
    }

    private fun calculateSquadCenter(squad: Squad): Pair<Float, Float> {
        if (squad.memberAgentIds.isEmpty()) return Pair(1000f, 1000f)
        var sumX = 0f; var sumY = 0f
        squad.memberAgentIds.forEach { id ->
            sumX += buffer.x[id.toInt()]
            sumY += buffer.y[id.toInt()]
        }
        return Pair(sumX / squad.memberAgentIds.size, sumY / squad.memberAgentIds.size)
    }

    fun stop() { 
        isRunning = false
        engineScope.cancel() 
    }

    fun getInfluenceMap() = influenceMap
    
    fun getEconomyStatus() = economyEngine.getStatus()
    
    fun getReplay() = replaySystem
    
    fun getPerformanceReport() = analytics.calculateMetrics(syncToModelList())
    
    fun getExplanationLogs() = explanationSystem.getLatestExplanations()
}
