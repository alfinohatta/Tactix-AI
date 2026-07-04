package com.example.tactixai.core.analytics

import android.util.Log

/**
 * Memonitor performa simulasi secara real-time.
 * Memastikan target 10.000+ agen tercapai dengan FPS yang stabil.
 */
class PerformanceMonitor {
    private var frameCount = 0
    private var startTime = System.currentTimeMillis()
    private var lastLogTime = System.currentTimeMillis()
    
    var currentFps = 0
    var avgProcessingTimeMs = 0f
    
    private val processingTimes = mutableListOf<Long>()

    fun startFrame() {
        startTime = System.currentTimeMillis()
    }

    fun endFrame() {
        val endTime = System.currentTimeMillis()
        processingTimes.add(endTime - startTime)
        frameCount++

        val now = System.currentTimeMillis()
        if (now - lastLogTime >= 1000) {
            currentFps = frameCount
            avgProcessingTimeMs = processingTimes.average().toFloat()
            
            Log.d("TactixPerf", "FPS: $currentFps | Avg Logic Time: ${String.format("%.2f", avgProcessingTimeMs)}ms | Agents: Tracking...")
            
            frameCount = 0
            processingTimes.clear()
            lastLogTime = now
        }
    }
}
