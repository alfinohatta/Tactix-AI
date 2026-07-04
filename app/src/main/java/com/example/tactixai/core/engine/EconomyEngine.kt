package com.example.tactixai.core.engine

import com.example.tactixai.core.model.FormationType

/**
 * Layer 13: Economy System.
 * Mengelola biaya sumber daya untuk setiap aksi taktis.
 * Biaya disesuaikan berdasarkan kompleksitas dari 100 formasi.
 */
class EconomyEngine {

    data class Resources(
        var energy: Float = 1000f,
        var information: Float = 100f, // Digunakan untuk aktivasi formasi & AI thinking
        var informationCapacity: Float = 200f
    )

    private var currentResources = Resources()

    /**
     * Menghitung biaya informasi berdasarkan kategori formasi (100 Formations).
     */
    fun getFormationInfoCost(type: FormationType): Float {
        return when {
            // J. EXPERIMENTAL (High Cost)
            type.name.startsWith("OMEGA") || type.name.startsWith("BLACK_HOLE") -> 50f
            // H. FUTURISTIC AI
            type.name.startsWith("HIVE_MIND") || type.name.startsWith("NEURAL") -> 40f
            // A. OFFENSIVE (Standard)
            type.name.startsWith("SPEARHEAD") || type.name.startsWith("WEDGE") -> 10f
            else -> 15f
        }
    }

    fun canAffordManeuver(cost: Float): Boolean = currentResources.information >= cost

    fun consumeInformation(cost: Float) {
        currentResources.information -= cost
    }

    fun regenerate(deltaTime: Float) {
        // Regenerasi berdasarkan intelijen lapangan (0.5 unit/detik)
        currentResources.information = (currentResources.information + 0.5f * deltaTime)
            .coerceAtMost(currentResources.informationCapacity)
    }

    fun getStatus() = currentResources
}
