package com.example.tactixai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tactixai.core.network.TactixNetworkManager
import com.example.tactixai.core.network.DeltaSyncEngine
import com.example.tactixai.core.SimulationManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola sinkronisasi Multiplayer.
 * Menghubungkan SimOS dengan pemain lain via Cloud MySQL/WebSocket.
 */
class MultiplayerViewModel(
    private val networkManager: TactixNetworkManager,
    private val deltaSync: DeltaSyncEngine
) : ViewModel() {

    fun connectToTournament(url: String, manager: SimulationManager) {
        networkManager.connect(url)
        
        // 1. Kirim Delta Updates (Hanya perubahan) ke lawan untuk efisiensi
        viewModelScope.launch {
            manager.agents.collectLatest { _ ->
                // Gunakan DeltaSyncEngine untuk menghitung apa yang perlu dikirim
                val delta = deltaSync.calculateDelta(manager.getBuffer())
                if (delta != null) {
                    // networkManager.sendDelta(delta) 
                }
            }
        }

        // 2. Terima Strategic Commands dari lawan
        viewModelScope.launch {
            networkManager.incomingState.collectLatest { _ ->
                // Injeksi state lawan ke Simulation Engine lokal
            }
        }
    }

    fun issueStrategicOrder(squadId: Long, formation: String, x: Float, y: Float) {
        networkManager.broadcastCommand(squadId, formation, x, y)
    }

    override fun onCleared() {
        super.onCleared()
        networkManager.disconnect()
    }
}
