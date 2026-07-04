import kotlin.ranges.coerceIn

// InfluenceMap implementation for TactixAI

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

            // +1.0 untuk kawan, -1.0 untuk lawan (dipengaruhi HP & Confidence)
            val weight = if (buffer.commanderId[i] == 1L) 1f else -1f
            val strength = (buffer.health[i] / 100f) * buffer.confidence[i] * weight

            grid[row * cols + col] += strength
        }
        applyBlurPass() // Menghasilkan 'Gradient' untuk navigasi AI
    }

    /**
     * Mencari 'Weak Point' di pertahanan musuh (Layer 6: Emergent Strategy).
     */
    fun findStrategicGaps(): List<Pair<Float, Float>> {
        val gaps = kotlin.collections.mutableListOf<Pair<Float, Float>>()
        // Implementasi Gradient Descent untuk mencari lembah pengaruh musuh
        return gaps
    }
}