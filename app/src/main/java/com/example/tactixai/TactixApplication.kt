package com.example.tactixai

import android.app.Application
import com.example.tactixai.di.AppContainer

/**
 * Tactix AI Platform Entry Point.
 * Menginisialisasi SimOS Container dan Cloud Bridge ke MySQL.
 */
class TactixApplication : Application() {
    
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer.getInstance(this)
    }
}
