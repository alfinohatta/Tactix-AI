package com.example.tactixai.data.repository

import com.example.tactixai.data.local.dao.*
import com.example.tactixai.data.local.entities.*
import com.example.tactixai.data.remote.TactixApiService
import com.example.tactixai.core.model.SimulationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Ultimate SimOS Data Hub.
 * Menghubungkan ekosistem lokal (Room) dengan infrastruktur Cloud (MySQL).
 */
class TactixRepository(
    private val simulationDao: SimulationDao,
    private val agentDao: AgentDao,
    private val apiService: TactixApiService
) {
    val allSimulations: Flow<List<SimulationEntity>> = simulationDao.getAllSimulations()

    suspend fun getSimulation(id: Long): SimulationEntity? = simulationDao.getSimulationById(id)

    /**
     * Sinkronisasi Hasil Akhir ke tabel 'simulation_results' di MySQL.
     */
    suspend fun syncOutcomeToMySQL(result: SimulationResultEntity): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<Unit> = apiService.storeFinalOutcome(result)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Sinkronisasi Doktrin Komandan ke tabel 'commanders' di MySQL.
     */
    suspend fun syncCommandersToCloud(commanders: List<CommanderEntity>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<Unit> = apiService.syncCommanders(commanders)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Mencatat keputusan AI secara batch ke tabel 'ai_decisions'.
     */
    suspend fun uploadAIDecisions(decisions: List<AIDecisionEntity>): Boolean = withContext(Dispatchers.IO) {
        if (decisions.isEmpty()) return@withContext true
        return@withContext try {
            val response: Response<Unit> = apiService.logAIDecisions(decisions)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun checkPlatformAccess(apiKey: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response: Response<Boolean> = apiService.validateApiKey(apiKey)
            response.body() ?: false
        } catch (e: Exception) {
            false
        }
    }
}
