package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Layer 9: AI Explanation System.
 * Mengubah data kognitif mentah menjadi narasi strategis.
 */
class StrategicNarrator {

    data class Insight(
        val timestamp: Long,
        val entityName: String,
        val reason: String,
        val impact: String,
        val confidence: Float
    )

    private val logs = ConcurrentLinkedQueue<Insight>()

    /**
     * Menghasilkan penjelasan sesuai Blueprint: 
     * "Reason: Enemy detected on east side. New formation increases survival probability by 18%."
     */
    fun generateInsight(
        squad: Squad,
        detectedThreatSide: String,
        weakness: Int,
        survivalGain: Int,
        confidence: Float
    ): Insight {
        val insight = Insight(
            timestamp = System.currentTimeMillis(),
            entityName = squad.name,
            reason = "Enemy detected on $detectedThreatSide side. Current formation weakness: $weakness%.",
            impact = "New formation increases survival probability by $survivalGain%.",
            confidence = confidence
        )
        logs.add(insight)
        if (logs.size > 100) logs.poll()
        return insight
    }

    fun getLatestInsights(): List<Insight> = logs.toList().reversed()
}
