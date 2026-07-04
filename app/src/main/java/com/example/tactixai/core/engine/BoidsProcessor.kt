package com.example.tactixai.core.engine

import com.example.tactixai.core.model.Agent
import kotlin.math.sqrt

/**
 * Mengimplementasikan Algoritma Boids untuk pergerakan kelompok yang natural.
 * Tiga aturan utama: Separation, Alignment, Cohesion.
 * Dioptimalkan untuk performa tinggi.
 */
class BoidsProcessor(
    var separationWeight: Float = 1.5f,
    var alignmentWeight: Float = 1.0f,
    var cohesionWeight: Float = 1.0f,
    var perceptionRadius: Float = 50.0f,
    var maxSpeed: Float = 5.0f
) {

    fun calculateNewVelocity(agent: Agent, neighbors: List<Agent>): Triple<Float, Float, Float> {
        if (neighbors.isEmpty()) return Triple(0f, 0f, 0f)

        var sepX = 0f; var sepY = 0f
        var aliX = 0f; var aliY = 0f
        var cohX = 0f; var cohY = 0f
        
        var neighborCount = 0

        for (other in neighbors) {
            val dist = distance(agent, other)
            if (dist > 0 && dist < perceptionRadius) {
                // Separation: Menghindari tabrakan
                val diffX = agent.x - other.x
                val diffY = agent.y - other.y
                sepX += diffX / dist
                sepY += diffY / dist

                // Alignment: Menyamakan arah dengan tetangga
                // (Mengasumsikan velocity disimpan di aiState atau dihitung dari delta)
                aliX += (other.aiState["vx"] as? Float ?: 0f)
                aliY += (other.aiState["vy"] as? Float ?: 0f)

                // Cohesion: Menuju pusat massa kelompok
                cohX += other.x
                cohY += other.y
                
                neighborCount++
            }
        }

        if (neighborCount == 0) return Triple(0f, 0f, 0f)

        sepX /= neighborCount
        sepY /= neighborCount

        aliX /= neighborCount
        aliY /= neighborCount

        cohX = (cohX / neighborCount) - agent.x
        cohY = (cohY / neighborCount) - agent.y

        // Resulting force
        val resX = (sepX * separationWeight) + (aliX * alignmentWeight) + (cohX * cohesionWeight)
        val resY = (sepY * separationWeight) + (aliY * alignmentWeight) + (cohY * cohesionWeight)

        return Triple(resX, resY, 0f)
    }

    private fun distance(a1: Agent, a2: Agent): Float {
        val dx = a1.x - a2.x
        val dy = a1.y - a2.y
        return sqrt(dx * dx + dy * dy)
    }
}
