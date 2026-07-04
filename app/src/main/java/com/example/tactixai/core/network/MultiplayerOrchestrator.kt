package com.example.tactixai.core.network

import com.example.tactixai.core.SimulationManager
import com.example.tactixai.core.model.Agent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Layer 12: Multiplayer Orchestrator.
 * Menghubungkan SimOS dengan pemain lain di internet.
 */
class MultiplayerOrchestrator(
    private val manager: SimulationManager,
    private val networkManager: TactixNetworkManager
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startSync() {
        // Broadcast local agents state periodically
        scope.launch {
            manager.agents.collectLatest { agents ->
                networkManager.broadcastLocalState(agents)
            }
        }

        // Receive remote updates
        scope.launch {
            networkManager.incomingState.collectLatest { remoteAgents ->
                applyRemoteUpdates(remoteAgents)
            }
        }
    }

    private fun applyRemoteUpdates(remoteAgents: List<Agent>) {
        // Sync remote agents into local physics buffer
    }
}
