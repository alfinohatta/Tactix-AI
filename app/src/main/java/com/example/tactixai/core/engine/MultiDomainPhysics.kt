package com.example.tactixai.core.engine

import com.example.tactixai.core.model.AgentDomain

/**
 * Mengelola hukum fisika spesifik untuk setiap Domain (Air, Land, Sea, Underwater).
 * Kunci untuk 'Tactix Simulation Engine' general-purpose.
 */
class MultiDomainPhysics {

    data class DomainConstants(
        val drag: Float,
        val maxSpeed: Float,
        val acceleration: Float,
        val visibilityMult: Float
    )

    private val domainRules = mapOf(
        AgentDomain.DRONE to DomainConstants(0.05f, 150f, 10f, 1.2f),    // Air: Fast, low drag
        AgentDomain.ROBOT to DomainConstants(0.25f, 60f, 5f, 0.8f),     // Land: Slow, high friction
        AgentDomain.SUBMARINE to DomainConstants(0.45f, 40f, 3f, 0.5f)  // Underwater: Heavy drag, low visibility
    )

    fun applyPhysics(
        domain: AgentDomain,
        vx: Float,
        vy: Float,
        ax: Float,
        ay: Float,
        deltaTime: Float
    ): Pair<Float, Float> {
        val rules = domainRules[domain] ?: domainRules[AgentDomain.DRONE]!!
        
        // V' = V + (A - Drag * V) * dt
        val newVx = vx + (ax - rules.drag * vx) * deltaTime
        val newVy = vy + (ay - rules.drag * vy) * deltaTime
        
        // Clamp speed
        val currentSpeed = kotlin.math.sqrt(newVx * newVx + newVy * newVy)
        return if (currentSpeed > rules.maxSpeed) {
            Pair((newVx / currentSpeed) * rules.maxSpeed, (newVy / currentSpeed) * rules.maxSpeed)
        } else {
            Pair(newVx, newVy)
        }
    }
}
