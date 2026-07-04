package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "simulation_events",
    foreignKeys = [
        ForeignKey(
            entity = SimulationEntity::class,
            parentColumns = ["id"],
            childColumns = ["simulationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("simulationId")]
)
data class SimulationEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val eventType: String, // SPAWN, MOVEMENT, FORMATION_CHANGE, ENGAGEMENT, LOSS, OBJECTIVE
    val payloadJson: String, // Menyimpan detail event dalam JSON
    val createdAt: Long = System.currentTimeMillis()
)
