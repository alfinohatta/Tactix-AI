package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.CommanderPersonality
import com.example.tactixai.core.model.FormationType

/**
 * Mendefinisikan aturan perilaku untuk AI Commander.
 * Implementasi Layer 4: AI Commander Personality.
 */
interface Doctrine {
    val preferredFormations: List<FormationType>
    val riskTolerance: Float // 0.0 to 1.0
    val aggressionLevel: Float
    val intelligenceWeight: Float // Prioritas gathering info vs attacking
    
    fun evaluateStrategicShift(currentSurvivalRate: Float, enemyVisibility: Float): Boolean
}

class AggressiveDoctrine : Doctrine {
    override val preferredFormations = listOf(FormationType.SPEARHEAD, FormationType.WEDGE, FormationType.NEEDLE, FormationType.HAMMER)
    override val riskTolerance = 0.8f
    override val aggressionLevel = 0.9f
    override val intelligenceWeight = 0.2f
    override fun evaluateStrategicShift(currentSurvivalRate: Float, enemyVisibility: Float) = currentSurvivalRate < 0.1f
}

class DefensiveDoctrine : Doctrine {
    override val preferredFormations = listOf(FormationType.SHIELD_WALL, FormationType.TURTLE, FormationType.FORTRESS_RING, FormationType.BASTION)
    override val riskTolerance = 0.2f
    override val aggressionLevel = 0.3f
    override val intelligenceWeight = 0.4f
    override fun evaluateStrategicShift(currentSurvivalRate: Float, enemyVisibility: Float) = currentSurvivalRate < 0.7f
}

class IntelligenceDoctrine : Doctrine {
    override val preferredFormations = listOf(FormationType.EAGLE, FormationType.OWL, FormationType.BAT, FormationType.GHOST, FormationType.PHANTOM_SWARM)
    override val riskTolerance = 0.4f
    override val aggressionLevel = 0.2f
    override val intelligenceWeight = 1.0f
    override fun evaluateStrategicShift(currentSurvivalRate: Float, enemyVisibility: Float) = enemyVisibility < 0.4f
}

class BalancedDoctrine : Doctrine {
    override val preferredFormations = listOf(FormationType.DIAMOND, FormationType.FANG_ZHEN, FormationType.ADAPTIVE)
    override val riskTolerance = 0.5f
    override val aggressionLevel = 0.5f
    override val intelligenceWeight = 0.5f
    override fun evaluateStrategicShift(currentSurvivalRate: Float, enemyVisibility: Float) = currentSurvivalRate < 0.5f
}

object DoctrineFactory {
    fun get(personality: CommanderPersonality): Doctrine {
        return when (personality) {
            CommanderPersonality.AGGRESSIVE -> AggressiveDoctrine()
            CommanderPersonality.DEFENSIVE -> DefensiveDoctrine()
            CommanderPersonality.INTELLIGENCE -> IntelligenceDoctrine()
            CommanderPersonality.BALANCED -> BalancedDoctrine()
            else -> BalancedDoctrine()
        }
    }
}
