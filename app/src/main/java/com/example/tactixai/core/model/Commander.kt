package com.example.tactixai.core.model

/**
 * Commander AI yang menentukan doktrin dan strategi tingkat tinggi.
 */
data class Commander(
    val id: Long,
    val simulationId: Long,
    val name: String,
    val personality: CommanderPersonality,
    val intelligenceLevel: Int = 50,
    val decisionLatencyMs: Int = 100
)

enum class CommanderPersonality {
    AGGRESSIVE,
    DEFENSIVE,
    ADAPTIVE,
    INTELLIGENCE,
    BALANCED
}
