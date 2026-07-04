package com.example.tactixai.core.intelligence

import com.example.tactixai.core.engine.AgentStateBuffer
import kotlin.math.abs

/**
 * Layer 3: Strategic Heatmap.
 * Mengubah 10.000+ data agen menjadi 'Influence Map' global.
 * Memungkinkan AI Commander untuk melihat medan perang secara makro.
 */
class InfluenceMap(val width: Float, val height: Float, val cellSize: Float = 50f) {
    private val cols = (width / cellSize).toInt() + 1
    private val rows = (height / cellSize).toInt() + 1
    private val grid = FloatArray(cols * rows)

    /**
     * Update heatmap menggunakan data dari primitive buffer (DOD Optimization).
     */
    fun updateFromBuffer(buffer: AgentStateBuffer) {
        grid.fill(0f)
        for (i in 0 until buffer.capacity) {
            if (!buffer.active[i]) continue

            val col = (buffer.x[i] / cellSize).toInt().coerceIn(0, cols - 1)
            val row = (buffer.y[i] / cellSize).toInt().coerceIn(0, rows - 1)

            // +1.0 untuk kawan (Id 1), -1.0 untuk lawan (Id lain)
            // Dipengaruhi oleh HP & Confidence sesuai Blueprint
            val weight = if (buffer.commanderId[i] == 1L) 1f else -1f
            val strength = (buffer.health[i] / 100f) * buffer.confidence[i] * weight

            grid[row * cols + col] += strength
        }
        applyBlurPass() // Menghasilkan 'Gradient' untuk navigasi AI
    }

    private fun applyBlurPass() {
        val newGrid = grid.copyOf()
        for (r in 1 until rows - 1) {
            for (c in 1 until cols - 1) {
                val idx = r * cols + c
                // Simple 3x3 box blur for influence spreading
                var sum = grid[idx]
                sum += grid[idx - 1] + grid[idx + 1]
                sum += grid[idx - cols] + grid[idx + cols]
                newGrid[idx] = sum / 5f
            }
        }
        System.arraycopy(newGrid, 0, grid, 0, grid.size)
    }

    /**
     * Mencari 'Weak Point' di pertahanan musuh (Layer 6: Emergent Strategy).
     * Area dengan pengaruh musuh rendah tapi dikelilingi pengaruh musuh.
     */
    fun findStrategicGaps(): List<Pair<Float, Float>> {
        val gaps = mutableListOf<Pair<Float, Float>>()
        for (r in 1 until rows - 1) {
            for (c in 1 until cols - 1) {
                val idx = r * cols + c
                val valSelf = grid[idx]
                // Jika area netral tapi dekat musuh
                if (abs(valSelf) < 0.2f) {
                    val neighborsMusuh = countEnemyNeighbors(c, r)
                    if (neighborsMusuh >= 2) {
                        gaps.add(Pair(c * cellSize, r * cellSize))
                    }
                }
            }
        }
        return gaps
    }

    private fun countEnemyNeighbors(c: Int, r: Int): Int {
        var count = 0
        if (grid[r * cols + (c - 1)] < -0.5f) count++
        if (grid[r * cols + (c + 1)] < -0.5f) count++
        if (grid[(r - 1) * cols + c] < -0.5f) count++
        if (grid[(r + 1) * cols + c] < -0.5f) count++
        return count
    }

    fun getInfluenceAt(x: Float, y: Float): Float {
        val c = (x / cellSize).toInt().coerceIn(0, cols - 1)
        val r = (y / cellSize).toInt().coerceIn(0, rows - 1)
        return grid[r * cols + c]
    }
}
