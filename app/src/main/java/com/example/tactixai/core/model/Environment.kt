package com.example.tactixai.core.model

/**
 * Layer 1: Simulation World
 */
data class Environment(
    val id: Long,
    val simulationId: Long,
    val type: EnvironmentType,
    val width: Int,
    val height: Int,
    val terrain: TerrainData,
    val weather: WeatherData
)

enum class EnvironmentType {
    AIR, LAND, SEA, UNDERWATER, HYBRID
}

data class TerrainData(
    val oceanPercentage: Int,
    val mountainPercentage: Int,
    val openLandPercentage: Int,
    val visibility: Float // 0.0 to 1.0
)

data class WeatherData(
    val condition: String,
    val windSpeed: Float,
    val humidity: Float
)
