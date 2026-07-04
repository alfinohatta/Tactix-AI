package com.example.tactixai.core

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.tactixai.di.AppContainer

/**
 * Service untuk menjaga simulasi 10.000 agen tetap berjalan di background.
 */
class SimulationService : Service() {

    private var simulationManager: SimulationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == "STOP_SIMULATION") {
            simulationManager?.shutdown()
            stopSelf()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        simulationManager?.shutdown()
        super.onDestroy()
    }
}
