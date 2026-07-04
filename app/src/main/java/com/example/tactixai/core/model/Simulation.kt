package com.example.tactixai.core.model

/**
 * Representasi dari satu proyek simulasi.
 */
data class Simulation(
    val id: Long,
    val userId: Long,
    val name: String,
    val description: String?,
    var status: SimulationStatus = SimulationStatus.DRAFT,
    var simulationSpeed: Float = 1.0f,
    val maxAgents: Int = 1000
)

enum class SimulationStatus {
    DRAFT, RUNNING, PAUSED, COMPLETED, FAILED
}
