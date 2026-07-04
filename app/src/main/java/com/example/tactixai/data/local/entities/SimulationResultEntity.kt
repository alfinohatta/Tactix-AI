package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "simulation_results",
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
data class SimulationResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val successRate: Float,
    val efficiencyScore: Float,
    val survivalRate: Float,
    val resourceUsageJson: String,
    val aiSummary: String,
    val createdAt: Long = System.currentTimeMillis()
)
