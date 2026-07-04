package com.example.tactixai.core.intelligence

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Layer AI Explanation System.
 * Memberikan narasi logis di balik keputusan AI untuk transparansi user.
 */
data class AIExplanation(
    val timestamp: Long,
    val entityName: String,
    val action: String,
    val reason: String,
    val confidence: Float,
    val impact: String
)

class ExplanationSystem {
    private val logs = ConcurrentLinkedQueue<AIExplanation>()

    fun logDecision(
        entityName: String,
        action: String,
        reason: String,
        confidence: Float,
        impact: String
    ) {
        val explanation = AIExplanation(
            System.currentTimeMillis(),
            entityName,
            action,
            reason,
            confidence,
            impact
        )
        logs.add(explanation)
        
        // Simpan hanya 100 log terakhir untuk efisiensi UI
        if (logs.size > 100) {
            logs.poll()
        }
    }

    fun getLatestExplanations(): List<AIExplanation> {
        return logs.toList().reversed()
    }
}
