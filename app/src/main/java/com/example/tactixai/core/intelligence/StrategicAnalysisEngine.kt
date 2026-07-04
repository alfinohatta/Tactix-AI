package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.FormationType
import com.example.tactixai.core.model.Squad
import kotlin.math.max

/**
 * Layer 5: Strategic Analysis Engine.
 * Menganalisis efektivitas strategi secara real-time.
 * Memberikan metrik 'Why' untuk AI Explanation System.
 */
class StrategicAnalysisEngine {

    data class StrategyInsight(
        val currentWeakness: Float, // 0.0 - 1.0
        val survivalProbability: Float,
        val recommendedFormation: FormationType?,
        val reasoning: String
    )

    fun analyzeSquadSituation(
        squad: Squad,
        squadAgents: List<Agent>,
        nearbyEnemies: List<Agent>
    ): StrategyInsight {
        val totalHealth = squadAgents.sumOf { it.health }.toFloat()
        val avgHealth = if (squadAgents.isNotEmpty()) totalHealth / squadAgents.size else 0f
        
        val enemyCount = nearbyEnemies.size
        val squadSize = squadAgents.size
        
        // Kalkulasi Weakness berdasarkan rasio jumlah dan tekanan posisi
        val numericalDisadvantage = if (squadSize > 0) (enemyCount.toFloat() / squadSize).coerceIn(0f, 2f) else 2f
        val weakness = (numericalDisadvantage * 0.5f + (100f - avgHealth) / 200f).coerceIn(0f, 1f)
        
        // Survival Probability
        val survivalProb = (1.0f - weakness).coerceIn(0.1f, 0.99f)
        
        var reasoning = "Stable operation."
        var recommendation: FormationType? = null

        if (weakness > 0.4f) {
            reasoning = "Enemy density detected. Current formation weakness: ${(weakness * 100).toInt()}%."
            recommendation = FormationType.FORTRESS_RING
        } else if (enemyCount > 0 && survivalProb > 0.8f) {
            reasoning = "Strategic advantage identified. High confidence in offensive maneuver."
            recommendation = FormationType.SPEARHEAD
        }

        return StrategyInsight(
            currentWeakness = weakness,
            survivalProbability = survivalProb,
            recommendedFormation = recommendation,
            reasoning = reasoning
        )
    }
}
