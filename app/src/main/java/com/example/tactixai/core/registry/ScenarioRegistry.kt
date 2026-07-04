package com.example.tactixai.core.registry

import com.example.tactixai.core.model.*

/**
 * Layer 15: Scenario Registry (General-Purpose SimOS).
 * Pusat kendali untuk beralih antar industri/domain.
 */
object ScenarioRegistry {

    data class SimScenario(
        val id: String,
        val title: String,
        val category: String, // MILITARY, LOGISTICS, TRAFFIC
        val defaultRules: List<GlobalRule>,
        val physicsMultiplier: Float,
        val successMetric: String
    )

    private val activeScenarios = mutableMapOf<String, SimScenario>()

    init {
        // Industri 1: Pertahanan (MILITARY)
        register(SimScenario(
            "ISLAND_WAR", "Operation Island Reach", "MILITARY",
            listOf(GlobalRule("ENERGY_DRAIN", 2.0f, emptyList())),
            1.2f, "SURVIVAL_RATE"
        ))

        // Industri 2: Logistik (LOGISTICS)
        register(SimScenario(
            "SMART_WAREHOUSE", "Logistics 4.0", "LOGISTICS",
            listOf(GlobalRule("COLLISION_PENALTY", 10.0f, listOf(AgentDomain.ROBOT))),
            0.8f, "EFFICIENCY_SCORE"
        ))

        // Industri 3: Lalu Lintas (TRAFFIC)
        register(SimScenario(
            "URBAN_TRAFFIC", "Autonomous City", "TRAFFIC",
            listOf(GlobalRule("FLOW_STABILITY", 1.0f, listOf(AgentDomain.ROBOT))),
            1.0f, "THROUGHPUT"
        ))
    }

    fun register(scenario: SimScenario) { activeScenarios[scenario.id] = scenario }
    fun get(id: String) = activeScenarios[id]
    fun listAll() = activeScenarios.values.toList()
}
