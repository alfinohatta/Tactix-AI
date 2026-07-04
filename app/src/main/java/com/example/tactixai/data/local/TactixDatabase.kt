package com.example.tactixai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tactixai.data.local.dao.*
import com.example.tactixai.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        UserDeviceEntity::class,
        SimulationEntity::class,
        EnvironmentEntity::class,
        CommanderEntity::class,
        AgentEntity::class,
        FormationEntity::class,
        FormationMemberEntity::class,
        AIDecisionEntity::class,
        SimulationEventEntity::class,
        SimulationResultEntity::class,
        SubscriptionEntity::class,
        APIKeyEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TactixDatabase : RoomDatabase() {

    abstract fun simulationDao(): SimulationDao
    abstract fun agentDao(): AgentDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TactixDatabase? = null

        fun getDatabase(context: Context): TactixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TactixDatabase::class.java,
                    "tactix_ai_database"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
