package com.example.tactixai.core.engine

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Flow Field Pathfinding.
 * Solusi terbaik untuk 10.000+ agen menuju target yang sama.
 * Menghindari kalkulasi A* individual yang sangat berat.
 */
class FlowField(
    private val rows: Int,
    private val cols: Int,
    private val cellSize: Float
) {
    // Grid arah (vektor satuan)
    private val field = Array(rows) { Array(cols) { Pair(0f, 0f) } }
    
    // Grid biaya (cost to target)
    private val costField = Array(rows) { IntArray(cols) { Int.MAX_VALUE } }

    /**
     * Menghasilkan flow field menuju target (x, y).
     * Menggunakan algoritma Dijkstra-like untuk cost field, lalu gradient descent untuk flow.
     */
    fun generate(targetX: Float, targetY: Float, terrainWeights: Array<IntArray>?) {
        val targetCol = (targetX / cellSize).toInt().coerceIn(0, cols - 1)
        val targetRow = (targetY / cellSize).toInt().coerceIn(0, rows - 1)

        // 1. Reset
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                costField[r][c] = Int.MAX_VALUE
            }
        }

        // 2. Calculate Cost Field (Dijkstra)
        val queue = mutableListOf<Pair<Int, Int>>()
        costField[targetRow][targetCol] = 0
        queue.add(Pair(targetRow, targetCol))

        var head = 0
        while (head < queue.size) {
            val (r, c) = queue[head++]
            val currentCost = costField[r][c]

            // Check 4 neighbors
            val neighbors = listOf(Pair(r - 1, c), Pair(r + 1, c), Pair(r, c - 1), Pair(r, c + 1))
            for (n in neighbors) {
                val nr = n.first
                val nc = n.second
                if (nr in 0 until rows && nc in 0 until cols) {
                    val terrainCost = terrainWeights?.get(nr)?.get(nc) ?: 1
                    val newCost = currentCost + terrainCost
                    if (newCost < costField[nr][nc]) {
                        costField[nr][nc] = newCost
                        queue.add(Pair(nr, nc))
                    }
                }
            }
        }

        // 3. Calculate Flow Field (Gradient)
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (costField[r][c] == Int.MAX_VALUE) {
                    field[r][c] = Pair(0f, 0f)
                    continue
                }

                // Cari tetangga dengan cost terkecil
                var minCost = costField[r][c]
                var bestDir = Pair(0f, 0f)

                for (dr in -1..1) {
                    for (dc in -1..1) {
                        val nr = r + dr
                        val nc = c + dc
                        if (nr in 0 until rows && nc in 0 until cols) {
                            if (costField[nr][nc] < minCost) {
                                minCost = costField[nr][nc]
                                bestDir = Pair(dc.toFloat(), dr.toFloat())
                            }
                        }
                    }
                }
                
                // Normalisasi vektor
                val mag = kotlin.math.sqrt(bestDir.first * bestDir.first + bestDir.second * bestDir.second)
                if (mag > 0) {
                    field[r][c] = Pair(bestDir.first / mag, bestDir.second / mag)
                } else {
                    field[r][c] = Pair(0f, 0f)
                }
            }
        }
    }

    fun getDirection(x: Float, y: Float): Pair<Float, Float> {
        val c = (x / cellSize).toInt().coerceIn(0, cols - 1)
        val r = (y / cellSize).toInt().toInt().coerceIn(0, rows - 1)
        return field[r][c]
    }
}
