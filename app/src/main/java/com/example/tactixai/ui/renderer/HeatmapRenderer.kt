package com.example.tactixai.ui.renderer

import android.graphics.*
import com.example.tactixai.core.intelligence.InfluenceMap
import kotlin.math.abs

/**
 * Merender 'Strategic Heatmap' di atas medan perang.
 * Visualisasi dominasi: Biru (Kawan) vs Merah (Lawan).
 */
class HeatmapRenderer(private val influenceMap: InfluenceMap) {

    private val gridPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    fun render(canvas: Canvas, screenWidth: Int, screenHeight: Int, cellSize: Float) {
        val cols = (screenWidth / cellSize).toInt()
        val rows = (screenHeight / cellSize).toInt()

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val influence = influenceMap.getInfluenceAt(c * cellSize, r * cellSize)
                if (influence == 0f) continue

                // Interpolasi Warna berdasarkan kekuatan pengaruh
                val intensity = (abs(influence) * 150).toInt().coerceIn(0, 255)
                gridPaint.color = if (influence > 0) {
                    Color.argb(intensity, 0, 150, 255) // Biru untuk Kawan
                } else {
                    Color.argb(intensity, 255, 50, 0)  // Merah untuk Lawan
                }

                canvas.drawRect(
                    c * cellSize, r * cellSize,
                    (c + 1) * cellSize, (r + 1) * cellSize,
                    gridPaint
                )
            }
        }
    }
}
