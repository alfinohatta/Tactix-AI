package com.example.tactixai.core.model

/**
 * Representasi deklaratif dari sebuah ekosistem simulasi (SimOS Blueprint).
 * Menghubungkan input manusia (Human Input) dengan strategi otonom (AI Output).
 */
data class SimulationBlueprint(
    val name: String,
    val description: String,
    val mapTemplate: String, 
    val agentCount: Int,
    val initialFormation: FormationType,
    val rules: List<GlobalRule>,
    val objective: SimulationObjective,
    val commanderPersonality: CommanderPersonality,
    val informationBudget: Float,
    val energyScarcity: Float,
    val width: Int = 2000,
    val height: Int = 2000
)

object BlueprintFactory {
    fun createIslandInvasion(agents: Int): SimulationBlueprint {
        return SimulationBlueprint(
            name = "Operation Island Reach",
            description = "10k Agent Autonomy Test on Hybrid Terrain",
            mapTemplate = "ISLAND",
            agentCount = agents,
            initialFormation = FormationType.SPEARHEAD,
            rules = listOf(
                GlobalRule("ENERGY_DRAIN", 2.0f, listOf(AgentDomain.DRONE, AgentDomain.ROBOT, AgentDomain.SUBMARINE)),
                GlobalRule("ENVIRONMENTAL_FRICTION", 1.5f, listOf(AgentDomain.ROBOT))
            ),
            objective = SimulationObjective.AREA_CONTROL,
            commanderPersonality = CommanderPersonality.ADAPTIVE,
            informationBudget = 500f,
            energyScarcity = 0.3f
        )
    }
}

/**
 * Common Enums & Data Classes for the Platform.
 * Didefinisikan di sini sebagai Single Source of Truth.
 */
enum class SimulationObjective {
    AREA_CONTROL, ASSET_PROTECTION, RECONNAISSANCE, ELIMINATION, SURVIVAL_TEST
}

data class GlobalRule(
    val type: String,
    val magnitude: Float,
    val targetDomains: List<AgentDomain>
)
