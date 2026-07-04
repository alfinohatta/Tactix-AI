package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tactixai.core.model.EnvironmentType

@Entity(
    tableName = "environments",
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
data class EnvironmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val environmentType: EnvironmentType,
    val mapWidth: Int,
    val mapHeight: Int,
    val weatherJson: String,
    val terrainJson: String,
    val createdAt: Long = System.currentTimeMillis()
)
