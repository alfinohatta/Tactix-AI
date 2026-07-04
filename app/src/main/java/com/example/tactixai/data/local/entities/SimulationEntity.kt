package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tactixai.core.model.SimulationStatus

/**
 * Representasi tabel 'simulations' dari Production Schema.
 */
@Entity(tableName = "simulations")
data class SimulationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,
    val description: String?,
    val status: SimulationStatus,
    val simulationSpeed: Double = 1.0,
    val maxAgents: Int = 1000,
    val createdAt: Long = System.currentTimeMillis()
)
