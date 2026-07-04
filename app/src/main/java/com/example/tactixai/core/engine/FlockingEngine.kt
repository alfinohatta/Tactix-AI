package com.example.tactixai.core.engine

import kotlin.math.sqrt

/**
 * Layer 8: Physics & Movement.
 * Implementasi Boids Algorithm (Flocking Behavior) untuk koordinasi swarm agen.
 * Dioptimalkan untuk 10.000+ unit (Startup-Grade).
 * Aturan: Separation, Alignment, Cohesion.
 */
class FlockingEngine {

    /**
     * Menghitung gaya flocking untuk satu agen berdasarkan tetangganya.
     */
    fun calculateFlockingForce(
        index: Int,
        buffer: AgentStateBuffer,
        neighbors: List<Int>,
        weights: FlockingWeights = FlockingWeights()
    ): Pair<Float, Float> {
        if (neighbors.isEmpty()) return Pair(0f, 0f)

        var sepX = 0f; var sepY = 0f
        var aliX = 0f; var aliY = 0f
        var cohX = 0f; var cohY = 0f
        
        var sepCount = 0
        var aliCount = 0
        var cohCount = 0

        val px = buffer.x[index]
        val py = buffer.y[index]

        for (nIdx in neighbors) {
            if (nIdx == index) continue
            
            val nx = buffer.x[nIdx]
            val ny = buffer.y[nIdx]
            
            val dx = px - nx
            val dy = py - ny
            val distSq = dx * dx + dy * dy
            
            if (distSq == 0f) continue

            // 1. Separation (Avoid crowding)
            if (distSq < weights.sepDist * weights.sepDist) {
                val dist = sqrt(distSq)
                sepX += dx / dist
                sepY += dy / dist
                sepCount++
            }

            // 2. Alignment (Steer towards average heading)
            if (distSq < weights.aliDist * weights.aliDist) {
                aliX += buffer.vx[nIdx]
                aliY += buffer.vy[nIdx]
                aliCount++
            }

            // 3. Cohesion (Steer towards center of mass)
            if (distSq < weights.cohDist * weights.cohDist) {
                cohX += nx
                cohY += ny
                cohCount++
            }
        }

        var fx = 0f
        var fy = 0f

        if (sepCount > 0) {
            fx += (sepX / sepCount) * weights.sepWeight
            fy += (sepY / sepCount) * weights.sepWeight
        }

        if (aliCount > 0) {
            fx += (aliX / aliCount) * weights.aliWeight
            fy += (aliY / aliCount) * weights.aliWeight
        }

        if (cohCount > 0) {
            val centerX = cohX / cohCount
            val centerY = cohY / cohCount
            fx += (centerX - px) * weights.cohWeight
            fy += (centerY - py) * weights.cohWeight
        }

        return Pair(fx, fy)
    }

    data class FlockingWeights(
        val sepDist: Float = 25f,
        val aliDist: Float = 50f,
        val cohDist: Float = 50f,
        val sepWeight: Float = 2.0f,
        val aliWeight: Float = 1.0f,
        val cohWeight: Float = 0.5f
    )
}
