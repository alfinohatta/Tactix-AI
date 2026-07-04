package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tactixai.core.model.FormationPurpose
import com.example.tactixai.core.model.FormationType

@Entity(tableName = "formations")
data class FormationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val simulationId: Long,
    val name: String,
    val formationType: FormationType,
    val purpose: FormationPurpose,
    val parametersJson: String // Konfigurasi khusus formasi (spacing, radius, dll)
)
