package com.example.tactixai.core.engine

import kotlin.math.floor

/**
 * Spatial Partitioning menggunakan Grid yang dioptimalkan untuk Data-Oriented Design (DOD).
 * Menggunakan indeks agen (Int) alih-alih objek Agent untuk menghindari GC pressure.
 */
class SpatialGrid(
    private val width: Float,
    private val height: Float,
    private val cellSize: Float
) {
    private val cols = (width / cellSize).toInt() + 1
    private val rows = (height / cellSize).toInt() + 1
    
    // Grid menyimpan list indeks agen. 
    // Menggunakan ArrayList untuk akses cepat, namun idealnya menggunakan flat array untuk performa ekstrim.
    private val grid = Array(cols * rows) { mutableListOf<Int>() }

    fun clear() {
        for (cell in grid) {
            cell.clear()
        }
    }

    /**
     * Memasukkan indeks agen ke dalam grid berdasarkan posisinya.
     */
    fun insert(index: Int, x: Float, y: Float) {
        val col = floor(x / cellSize).toInt().coerceIn(0, cols - 1)
        val row = floor(y / cellSize).toInt().coerceIn(0, rows - 1)
        grid[row * cols + col].add(index)
    }

    /**
     * Mendapatkan indeks tetangga di sekitar koordinat (x, y).
     */
    fun getNeighborsIndices(x: Float, y: Float, radius: Float): List<Int> {
        val neighbors = mutableListOf<Int>()
        val startCol = floor((x - radius) / cellSize).toInt().coerceIn(0, cols - 1)
        val endCol = floor((x + radius) / cellSize).toInt().coerceIn(0, cols - 1)
        val startRow = floor((y - radius) / cellSize).toInt().coerceIn(0, rows - 1)
        val endRow = floor((y + radius) / cellSize).toInt().coerceIn(0, rows - 1)

        for (r in startRow..endRow) {
            for (c in startCol..endCol) {
                neighbors.addAll(grid[r * cols + c])
            }
        }
        return neighbors
    }
}
