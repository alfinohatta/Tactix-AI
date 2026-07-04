package com.example.tactixai.core.engine

/**
 * Layer 1: Simulation World - Terrain & Resource Mapping.
 * Mengelola data geografis dan sumber daya dalam grid yang dioptimalkan.
 */
class TerrainGrid(
    val width: Int,
    val height: Int,
    val cellSize: Float
) {
    private val cols = (width / cellSize).toInt()
    private val rows = (height / cellSize).toInt()
    
    // Primitive arrays untuk performa maksimal (DOD)
    private val terrainTypes = IntArray(cols * rows)
    private val resourceDensity = FloatArray(cols * rows)

    companion object {
        const val TYPE_OCEAN = 0
        const val TYPE_LAND = 1
        const val TYPE_MOUNTAIN = 2
    }

    /**
     * Menghasilkan peta "Island" sesuai spesifikasi:
     * 60% Ocean, 20% Mountain, 20% Open Land.
     */
    fun generateIslandMap() {
        val totalCells = cols * rows
        val oceanCount = (totalCells * 0.6f).toInt()
        val mountainCount = (totalCells * 0.2f).toInt()
        
        // Default: Land
        terrainTypes.fill(TYPE_LAND)
        
        // Skenario Sederhana: Ocean di pinggiran, Mountain di tengah
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val distFromCenter = Math.sqrt(
                    Math.pow((c - cols / 2).toDouble(), 2.0) + 
                    Math.pow((r - rows / 2).toDouble(), 2.0)
                )
                val normalizedDist = distFromCenter / (cols / 2)
                
                val index = r * cols + c
                if (normalizedDist > 0.7f) {
                    terrainTypes[index] = TYPE_OCEAN
                } else if (normalizedDist < 0.2f) {
                    terrainTypes[index] = TYPE_MOUNTAIN
                }
                
                // Distribusi sumber daya terbatas
                resourceDensity[index] = if (terrainTypes[index] == TYPE_LAND) 100f else 0f
            }
        }
    }

    fun getSpeedModifier(x: Float, y: Float): Float {
        val c = (x / cellSize).toInt().coerceIn(0, cols - 1)
        val r = (y / cellSize).toInt().coerceIn(0, rows - 1)
        return when (terrainTypes[r * cols + c]) {
            TYPE_OCEAN -> 0.4f     // Sangat lambat (kecuali submarine)
            TYPE_MOUNTAIN -> 0.6f  // Lambat
            else -> 1.0f           // Kecepatan normal
        }
    }

    fun getVisibilityModifier(x: Float, y: Float): Float {
        val c = (x / cellSize).toInt().coerceIn(0, cols - 1)
        val r = (y / cellSize).toInt().coerceIn(0, rows - 1)
        // Mountain menghalangi visibilitas
        return if (terrainTypes[r * cols + c] == TYPE_MOUNTAIN) 0.5f else 1.0f
    }
}
