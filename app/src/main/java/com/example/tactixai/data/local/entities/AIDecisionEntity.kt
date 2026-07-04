package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_decisions")
data class AIDecisionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val agentId: Long?,
    val decisionType: String,
    val reasoning: String,
    val confidence: Float,
    val createdAt: Long = System.currentTimeMillis()
)
