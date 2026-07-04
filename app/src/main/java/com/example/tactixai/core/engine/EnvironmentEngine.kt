package com.example.tactixai.core.engine

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.Environment
import com.example.tactixai.core.model.EnvironmentType

/**
 * Layer 2: Environment Engine.
 * Mengelola interaksi antara agen dan dunia (Terrain impact, Weather effects).
 */
class EnvironmentEngine(private val environment: Environment) {

    /**
     * Menghitung modifier kecepatan berdasarkan terrain di posisi tertentu.
     */
    fun getSpeedModifier(x: Float, y: Float): Float {
        // Implementasi sederhana: Mountain memperlambat pergerakan
        // Di sistem produksi, ini akan membaca noise map atau grid data terrain
        return when (environment.type) {
            EnvironmentType.LAND -> 1.0f
            EnvironmentType.AIR -> 1.2f // Udara lebih cepat
            EnvironmentType.SEA -> 0.8f // Air lebih lambat
            else -> 1.0f
        }
    }

    /**
     * Mengecek apakah sebuah agen terlihat berdasarkan visibility environment.
     */
    fun isVisible(agent: Agent, observerX: Float, observerY: Float): Boolean {
        val distSq = Math.pow((agent.x - observerX).toDouble(), 2.0) + 
                     Math.pow((agent.y - observerY).toDouble(), 2.0)
        
        // Visibility 0.7 berarti agen terlihat dalam radius tertentu yang dipengaruhi cuaca
        val maxDist = 500f * environment.terrain.visibility
        return distSq < (maxDist * maxDist)
    }

    fun applyEnvironmentalStress(agent: Agent, deltaTime: Float) {
        // Cuaca ekstrem menghabiskan energi lebih cepat
        if (environment.weather.windSpeed > 20f) {
            agent.energy -= (2 * deltaTime).toInt()
        }
    }
}
