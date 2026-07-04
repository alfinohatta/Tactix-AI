package com.example.tactixai.data.local.dao

import androidx.room.*
import com.example.tactixai.data.local.entities.SimulationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulationDao {
    @Query("SELECT * FROM simulations ORDER BY createdAt DESC")
    fun getAllSimulations(): Flow<List<SimulationEntity>>

    @Query("SELECT * FROM simulations WHERE id = :id")
    suspend fun getSimulationById(id: Long): SimulationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulation(simulation: SimulationEntity): Long

    @Delete
    suspend fun deleteSimulation(simulation: SimulationEntity): Int

    @Update
    suspend fun updateSimulation(simulation: SimulationEntity): Int
}
