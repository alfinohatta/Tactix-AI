package com.example.tactixai.data.local

import androidx.room.TypeConverter
import com.example.tactixai.core.model.*

class Converters {
    @TypeConverter
    fun fromSimulationStatus(value: SimulationStatus) = value.name
    @TypeConverter
    fun toSimulationStatus(value: String) = SimulationStatus.valueOf(value)

    @TypeConverter
    fun fromAgentDomain(value: AgentDomain) = value.name
    @TypeConverter
    fun toAgentDomain(value: String) = AgentDomain.valueOf(value)

    @TypeConverter
    fun fromAgentRole(value: AgentRole) = value.name
    @TypeConverter
    fun toAgentRole(value: String) = AgentRole.valueOf(value)

    @TypeConverter
    fun fromAgentStatus(value: AgentStatus) = value.name
    @TypeConverter
    fun toAgentStatus(value: String) = AgentStatus.valueOf(value)

    @TypeConverter
    fun fromCommanderPersonality(value: CommanderPersonality) = value.name
    @TypeConverter
    fun toCommanderPersonality(value: String) = CommanderPersonality.valueOf(value)
}
