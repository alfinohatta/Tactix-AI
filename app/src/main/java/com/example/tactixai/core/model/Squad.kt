package com.example.tactixai.core.model

/**
 * Level 2 AI: Squad Layer.
 * Menjembatani Commander (Strategis) dan Agents (Taktis).
 * Mengelola formasi dan koordinasi lokal.
 */
data class Squad(
    val id: Long,
    val commanderId: Long,
    var formationId: Long?,
    val name: String,
    val leaderAgentId: Long,
    val memberAgentIds: MutableList<Long> = mutableListOf(),
    
    // Squad-level objective
    var currentObjective: String = "IDLE",
    var status: SquadStatus = SquadStatus.STABLE
)

enum class SquadStatus {
    STABLE, ENGAGED, RETREATING, BROKEN
}
