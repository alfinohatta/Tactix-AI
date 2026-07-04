package com.example.tactixai.core.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result
import com.example.tactixai.di.AppContainer
import com.example.tactixai.data.local.entities.SimulationResultEntity
import com.google.gson.Gson

/**
 * Worker untuk sinkronisasi data ke MySQL Production di background.
 * Menjamin integritas data analitik platform sesuai blueprint Layer 5.
 */
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val container = AppContainer.getInstance(applicationContext)
        val resultJson = inputData.getString("result_json") ?: return Result.failure()
        
        return try {
            val resultEntity = Gson().fromJson(resultJson, SimulationResultEntity::class.java)
            
            // Push ke MySQL via Repository Cloud Bridge
            container.tactixRepository.syncOutcomeToMySQL(resultEntity)
            
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
