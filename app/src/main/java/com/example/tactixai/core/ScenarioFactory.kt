package com.example.tactixai.core

import com.example.tactixai.core.model.*
import java.util.Random

/**
 * Scenario Factory (Startup-Grade).
 * Membangun ekosistem simulasi otonom untuk berbagai domain (B2B & Gaming).
 */
object ScenarioFactory {

    /**
     * Skenario Militer: Swarm Drone Invasion.
     * Sekarang menyertakan Struktur Komando (Commander & Squads).
     */
    fun createSwarmInvasion(count: Int): Scenario {
        val agents = mutableListOf<Agent>()
        val squads = mutableListOf<Squad>()
        val commanders = mutableListOf<Commander>()
        val random = Random()

        val commanderId = 1L
        commanders.add(Commander(commanderId, 1L, "Ghost-Commander", CommanderPersonality.ADAPTIVE))

        val squadSize = 50
        val squadCount = count / squadSize

        for (s in 0 until squadCount) {
            val sId = (s + 1).toLong()
            val squad = Squad(
                id = sId,
                commanderId = commanderId,
                formationId = null,
                name = "Alpha-Squad-$s",
                leaderAgentId = (s * squadSize).toLong()
            )

            for (i in 0 until squadSize) {
                val aId = (s * squadSize + i).toLong()
                agents.add(Agent(
                    id = aId,
                    simulationId = 1L,
                    commanderId = commanderId,
                    squadId = sId,
                    name = "Drone-$aId",
                    domain = AgentDomain.DRONE,
                    role = AgentRole.ASSAULT,
                    x = random.nextFloat() * 2000f,
                    y = random.nextFloat() * 2000f,
                    status = AgentStatus.MOVING
                ))
                squad.memberAgentIds.add(aId)
            }
            squads.add(squad)
        }

        return Scenario(agents, squads, commanders)
    }

    data class Scenario(
        val agents: List<Agent>,
        val squads: List<Squad>,
        val commanders: List<Commander>
    )

    /**
     * Skenario B2B: Smart Warehouse Logistics.
     * Mensimulasikan robot gudang otonom yang bergerak dalam grid.
     */
    fun createLogisticsGrid(robotCount: Int): List<Agent> {
        val agents = mutableListOf<Agent>()
        for (i in 0 until robotCount) {
            agents.add(Agent(
                id = (10000 + i).toLong(),
                simulationId = 2L,
                commanderId = 2L, // Logistics AI Commander
                name = "AGV-$i", // Autonomous Guided Vehicle
                domain = AgentDomain.ROBOT,
                role = AgentRole.SUPPORT,
                x = (i % 10) * 100f,
                y = (i / 10) * 100f,
                status = AgentStatus.MOVING,
                currentGoal = "PICKUP_CARGO"
            ))
        }
        return agents
    }

    /**
     * Skenario B2B: Autonomous Traffic Simulation.
     * Mensimulasikan kendaraan otonom di persimpangan jalan.
     */
    fun createAutonomousTraffic(vehicleCount: Int): List<Agent> {
        val agents = mutableListOf<Agent>()
        val random = Random()
        for (i in 0 until vehicleCount) {
            agents.add(Agent(
                id = (20000 + i).toLong(),
                simulationId = 3L,
                commanderId = 3L, // Traffic OS Commander
                name = "AV-$i",
                domain = AgentDomain.ROBOT,
                role = AgentRole.SCOUT,
                x = random.nextFloat() * 5000f,
                y = random.nextFloat() * 5000f,
                status = AgentStatus.MOVING,
                currentGoal = "NAVIGATE_TO_DESTINATION"
            ))
        }
        return agents
    }
}
