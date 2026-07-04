package com.example.tactixai.core.model

/**
 * Representasi dari 'Ecosystem Design' yang dibuat oleh User.
 * Catatan: Menggunakan model Objective dan Rule yang didefinisikan secara terpusat 
 * di SimulationBlueprint.kt untuk menghindari Redeclaration.
 */
data class SimulationConfig(
    val name: String,
    val objective: SimulationObjective,
    val rules: List<GlobalRule>,
    val resources: ResourceSetup,
    val environment: EnvironmentConfig,
    val commanderConfigs: List<CommanderConfig>
)

data class ResourceSetup(
    val initialEnergy: Float,
    val informationRegenRate: Float,
    val maxCapacity: Int
)

data class EnvironmentConfig(
    val mapType: String,
    val visibilityLevel: Float,
    val hazardIntensity: Float
)

data class CommanderConfig(
    val name: String,
    val personality: CommanderPersonality,
    val initialSquads: Int
)
