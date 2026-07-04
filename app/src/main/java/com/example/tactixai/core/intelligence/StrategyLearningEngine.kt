package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.FormationType
import java.util.concurrent.ConcurrentHashMap

/**
 * Layer 5: AI Learning System.
 * Mengimplementasikan feedback loop: Action -> Result -> Reward.
 * Digunakan oleh AI Commanders untuk memilih strategi dengan 'Win Rate' tertinggi.
 */
class StrategyLearningEngine {

    // Menyimpan 'Quality Score' (Q-Table sederhana) untuk setiap Formasi di setiap Skenario
    private val strategyQTable = ConcurrentHashMap<String, Float>()

    /**
     * Mencatat hasil simulasi dan memberikan reward/penalty.
     */
    fun recordOutcome(
        scenarioType: String,
        formationUsed: FormationType,
        survivalRate: Float,
        energyEfficiency: Float
    ) {
        val key = "${scenarioType}_${formationUsed.name}"
        val currentScore = strategyQTable.getOrDefault(key, 0.5f)
        
        // Perhitungan Reward: Survival tinggi + Efisiensi tinggi = High Reward
        val reward = (survivalRate * 0.7f + energyEfficiency * 0.3f)
        
        // Learning Rate: 0.1 (Update gradual)
        val newScore = currentScore + 0.1f * (reward - currentScore)
        strategyQTable[key] = newScore
    }

    /**
     * Memberikan rekomendasi formasi terbaik berdasarkan pengalaman masa lalu.
     */
    fun recommendFormation(scenarioType: String): FormationType {
        return strategyQTable.filterKeys { it.startsWith(scenarioType) }
            .maxByOrNull { it.value }
            ?.let { 
                val typeName = it.key.substringAfter("${scenarioType}_")
                FormationType.valueOf(typeName)
            } ?: FormationType.ADAPTIVE
    }

    fun getConfidenceScore(scenarioType: String, formation: FormationType): Int {
        val score = strategyQTable.getOrDefault("${scenarioType}_${formation.name}", 0.5f)
        return (score * 100).toInt()
    }
}
