package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "agents",
    foreignKeys = [
        ForeignKey(
            entity = SimulationEntity::class,
            parentColumns = ["id"],
            childColumns = ["simulationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("simulationId"), Index("commanderId"), Index("positionX", "positionY")]
)
data class AgentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val commanderId: Long?,
    val name: String,
    val domain: String,
    val role: String,
    val health: Int = 100,
    val energy: Int = 100,
    val status: String = "IDLE",
    val positionX: Float,
    val positionY: Float,
    val positionZ: Float = 0f,
    val currentGoal: String = "IDLE",
    val threatLevel: String = "MEDIUM",
    val confidence: Float = 1.0f,
    val aiStateJson: String = "{}",
    val squadId: Long? = null
)

