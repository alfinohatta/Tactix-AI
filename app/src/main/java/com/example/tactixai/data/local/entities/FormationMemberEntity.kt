package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "formation_members",
    primaryKeys = ["formationId", "agentId"],
    foreignKeys = [
        ForeignKey(
            entity = FormationEntity::class,
            parentColumns = ["id"],
            childColumns = ["formationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AgentEntity::class,
            parentColumns = ["id"],
            childColumns = ["agentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("formationId"), Index("agentId")]
)
data class FormationMemberEntity(
    val formationId: Long,
    val agentId: Long,
    val positionOrder: Int,
    val role: String?
)
