package com.example.tactixai.data.remote

import com.example.tactixai.data.local.entities.*
import com.example.tactixai.core.model.SimulationBlueprint
import retrofit2.Response
import retrofit2.http.*

/**
 * Platform API Gateway (MySQL Bridge).
 * Interface ini selaras 1:1 dengan kebutuhan platform dan skema MySQL Production.
 */
interface TactixApiService {

    // --- USERS ---
    @POST("users/register")
    suspend fun registerUser(@Body user: UserEntity): Response<UserEntity>

    @GET("users/{id}/subscriptions")
    suspend fun getSubscriptionStatus(@Path("id") userId: Long): Response<SubscriptionEntity>

    // --- MARKETPLACE ---
    @GET("marketplace/blueprints")
    suspend fun getPublicBlueprints(): Response<List<SimulationBlueprint>>

    @POST("marketplace/publish")
    suspend fun publishBlueprint(@Body blueprint: SimulationBlueprint): Response<Unit>

    // --- AGENTS & COMMANDERS (MySQL Sync) ---
    @POST("commanders/sync")
    suspend fun syncCommanders(@Body commanders: List<CommanderEntity>): Response<Unit>

    // --- ANALYTICS ---
    @POST("analytics/results")
    suspend fun storeFinalOutcome(@Body result: SimulationResultEntity): Response<Unit>

    @POST("analytics/decisions")
    suspend fun logAIDecisions(@Body decisions: List<AIDecisionEntity>): Response<Unit>

    @GET("auth/validate_key")
    suspend fun validateApiKey(@Header("X-API-KEY") key: String): Response<Boolean>
}
