package com.example.tactixai.core.engine

import com.example.tactixai.core.model.AgentStatus

/**
 * Combat Engine yang dioptimalkan dengan Data-Oriented Design (DOD).
 * Beroperasi langsung pada AgentStateBuffer untuk menghindari alokasi objek.
 */
class CombatEngine {

    /**
     * Memproses interaksi tempur antar agen menggunakan data buffer mentah.
     */
    fun processCombatBuffer(buffer: AgentStateBuffer, spatialGrid: SpatialGrid, deltaTime: Float) {
        for (i in 0 until buffer.capacity) {
            if (!buffer.active[i] || buffer.health[i] <= 0) continue

            // Cari musuh menggunakan koordinat dari buffer
            val enemies = spatialGrid.getNeighborsIndices(buffer.x[i], buffer.y[i], radius = 30f)
            
            var targetFound = false
            for (enemyIdx in enemies) {
                // Pengecekan tim berdasarkan commanderId di buffer
                if (buffer.commanderId[enemyIdx] != buffer.commanderId[i] && buffer.active[enemyIdx]) {
                    // Deal damage (DOD Pass)
                    val damage = (15 * deltaTime).toInt().coerceAtLeast(1)
                    buffer.health[enemyIdx] -= damage
                    
                    // Mark as attacking
                    targetFound = true
                    break // Single target dps untuk efisiensi
                }
            }
            
            // Update status (Opsional: simpan status dalam array Int untuk performa lebih ekstrim)
            if (buffer.health[i] <= 0) {
                buffer.active[i] = false
            }
        }
    }
}
