package com.example.tactixai.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Client untuk koneksi ke Cloud MySQL API.
 */
object TactixRetrofitClient {
    private const val BASE_URL = "https://api.example.com/v1/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val instance: TactixApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Fixed reference
            .client(httpClient)
            .build()

        retrofit.create(TactixApiService::class.java)
    }
}
