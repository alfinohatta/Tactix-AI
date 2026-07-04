package com.example.tactixai.core.engine

import com.example.tactixai.core.model.Agent
import com.example.tactixai.core.model.AgentStatus

/**
 * Mengelola aturan dunia simulasi.
 * Memungkinkan Tactix AI menjadi platform simulasi umum.
 */
class RuleEngine {
    
    interface Rule {
        fun apply(agent: Agent, deltaTime: Float)
    }

    private val activeRules = mutableListOf<Rule>()

    fun addRule(rule: Rule) {
        activeRules.add(rule)
    }

    fun processRules(agents: List<Agent>, deltaTime: Float) {
        agents.forEach { agent ->
            activeRules.forEach { it.apply(agent, deltaTime) }
        }
    }
}

/**
 * Contoh Aturan Umum: Konsumsi Energi berdasarkan Pergerakan.
 */
class EnergyConsumptionRule : RuleEngine.Rule {
    override fun apply(agent: Agent, deltaTime: Float) {
        if (agent.status == AgentStatus.MOVING) {
            agent.energy -= (5 * deltaTime).toInt()
        }
    }
}

/**
 * Contoh Aturan Umum: Collision Avoidance (Aturan Fisika).
 */
class CollisionRule : RuleEngine.Rule {
    override fun apply(agent: Agent, deltaTime: Float) {
        // Logika untuk mencegah unit bertumpuk
    }
}
