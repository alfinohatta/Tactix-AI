package com.example.tactixai.core.analytics

import com.example.tactixai.core.model.Agent
import java.util.TreeMap

/**
 * Layer 10: Battle Replay System.
 * Menyimpan snapshot simulasi untuk dianalisis kembali.
 * Menggunakan TreeMap untuk pencarian frame berdasarkan waktu (Timeline).
 */
class BattleReplaySystem {

    data class ReplayFrame(
        val timestamp: Long,
        val agentPositions: List<AgentPosition>,
        val events: List<String>
    )

    data class AgentPosition(
        val id: Long,
        val x: Float,
        val y: Float,
        val status: String
    )

    private val timeline = TreeMap<Long, ReplayFrame>()

    fun recordFrame(timestamp: Long, agents: List<Agent>, events: List<String>) {
        val positions = agents.map { 
            AgentPosition(it.id, it.x, it.y, it.status.name) 
        }
        timeline[timestamp] = ReplayFrame(timestamp, positions, events)
    }

    fun getFrameAt(timestamp: Long): ReplayFrame? {
        // Mencari frame terdekat (floor key)
        val key = timeline.floorKey(timestamp) ?: return null
        return timeline[key]
    }

    fun clear() {
        timeline.clear()
    }
    
    fun getFullTimeline(): List<ReplayFrame> = timeline.values.toList()
}
