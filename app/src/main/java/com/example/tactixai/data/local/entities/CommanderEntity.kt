package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tactixai.core.model.CommanderPersonality

@Entity(tableName = "commanders")
data class CommanderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val name: String,
    val personality: CommanderPersonality,
    val intelligenceLevel: Int,
    val decisionLatencyMs: Int
)
