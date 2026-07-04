package com.example.tactixai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val username: String,
    val passwordHash: String,
    val plan: String = "FREE",
    val createdAt: Long = System.currentTimeMillis()
)
