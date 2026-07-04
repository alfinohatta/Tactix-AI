package com.example.tactixai.data.local.dao

import androidx.room.*
import com.example.tactixai.data.local.entities.AgentEntity

@Dao
interface AgentDao {
    @Query("SELECT * FROM agents WHERE simulationId = :simulationId")
    suspend fun getAgentsBySimulation(simulationId: Long): List<AgentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgents(agents: List<AgentEntity>)

    @Query("DELETE FROM agents WHERE simulationId = :simulationId")
    suspend fun deleteAgentsBySimulation(simulationId: Long): Int
}
