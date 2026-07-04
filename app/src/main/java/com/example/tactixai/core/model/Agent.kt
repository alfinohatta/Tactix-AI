package com.example.tactixai.core.model

/**
 * Representasi kognitif dan fisik dari sebuah Agent.
 * Implementasi Layer 2: Agent Intelligence.
 * Dioptimalkan untuk pemrosesan 10.000+ unit (Startup-Grade).
 */
data class Agent(
    val id: Long,
    val simulationId: Long,
    val commanderId: Long?,
    val name: String,
    val domain: AgentDomain,
    val role: AgentRole,
    
    // Core Stats
    var health: Int = 100,
    var energy: Int = 100,
    var status: AgentStatus = AgentStatus.IDLE,
    
    // Physics State (X, Y, Z support sesuai SQL Schema)
    var x: Float,
    var y: Float,
    var z: Float = 0f,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var vz: Float = 0f,
    
    // Cognitive State (Blueprint Layer 2 - A-00291 specifications)
    var currentGoal: String = "IDLE",
    var threatLevel: ThreatLevel = ThreatLevel.MEDIUM,
    var confidence: Float = 0.82f, // 0.0 to 1.0
    
    // Custom State (JSON di DB)
    val aiState: MutableMap<String, Any> = mutableMapOf(),
    val squadId: Long? = null
)


enum class AgentDomain { DRONE, ROBOT, SUBMARINE }
enum class AgentRole { SCOUT, ASSAULT, DEFENDER, SUPPORT, REPAIR, COMMANDER }
enum class AgentStatus { IDLE, MOVING, ATTACKING, DEFENDING, RETREATING, DESTROYED }
enum class ThreatLevel { LOW, MEDIUM, HIGH, CRITICAL }
