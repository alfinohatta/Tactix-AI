package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.FormationType
import java.util.concurrent.ConcurrentHashMap

/**
 * AI Learning System (Simplified Reinforcement Learning).
 * Belajar dari hasil simulasi sebelumnya untuk merekomendasikan strategi terbaik.
 */
class StrategyOptimizer {

    // Menyimpan score untuk setiap kombinasi strategi (Formation + Doctrine)
    private val strategyKnowledgeBase = ConcurrentHashMap<String, Float>()
    
    /**
     * Update knowledge base berdasarkan hasil simulasi.
     * Reward positif jika menang/efisien, negatif jika gagal.
     */
    fun recordResult(formation: FormationType, doctrine: String, reward: Float) {
        val key = "${formation.name}_$doctrine"
        val currentScore = strategyKnowledgeBase.getOrDefault(key, 50f)
        
        // Learning rate 0.1
        val newScore = currentScore + 0.1f * (reward - currentScore)
        strategyKnowledgeBase[key] = newScore
    }

    /**
     * Merekomendasikan strategi terbaik untuk kondisi saat ini.
     */
    fun suggestStrategy(): Pair<FormationType, String> {
        if (strategyKnowledgeBase.isEmpty()) {
            return Pair(FormationType.AERIAL_SPEAR, "BALANCED")
        }
        
        val bestEntry = strategyKnowledgeBase.maxByOrNull { it.value }
        val parts = bestEntry?.key?.split("_") ?: return Pair(FormationType.AERIAL_SPEAR, "BALANCED")
        
        return Pair(
            FormationType.valueOf(parts[0]),
            parts[1]
        )
    }

    fun getSuccessProbability(formation: FormationType, doctrine: String): Float {
        return strategyKnowledgeBase.getOrDefault("${formation.name}_$doctrine", 50f)
    }
}
