package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*
import com.example.tactixai.core.analytics.AnalyticsEngine

/**
 * Otak Komandan (Level 1 AI).
 * Mengimplementasikan Doktrin Strategis (Layer 4).
 */
class CommanderAI(
    val commander: Commander,
    private val doctrine: Doctrine,
    private val explanationSystem: ExplanationSystem
) {
    /**
     * Mengevaluasi kondisi pertempuran dan menentukan apakah perlu pergeseran strategi.
     */
    fun evaluateStrategy(agents: List<Agent>, metrics: AnalyticsEngine.SimulationMetrics) {
        // Hitung visibilitas musuh (proxy: rasio unit musuh yang terdeteksi)
        val enemyVisibility = if (agents.isNotEmpty()) 0.7f else 0.0f
        
        val shouldShift = doctrine.evaluateStrategicShift(
            currentSurvivalRate = metrics.survivalRate,
            enemyVisibility = enemyVisibility
        )

        if (shouldShift) {
            explanationSystem.logDecision(
                commander.name ?: "Unknown",
                "STRATEGIC_SHIFT",
                "Doctrine demands adaptation due to changing battlefield visibility ($enemyVisibility).",
                0.88f,
                "Goal: Maximize survival."
            )
        }
    }
}
