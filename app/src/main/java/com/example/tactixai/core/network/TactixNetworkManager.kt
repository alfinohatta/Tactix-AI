package com.example.tactixai.core.network

import com.example.tactixai.core.model.Agent
import com.google.gson.Gson
import okhttp3.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Layer 12: Multiplayer Real-time Sync.
 * Jembatan WebSocket untuk sinkronisasi state 10.000 agen.
 */
class TactixNetworkManager(private val client: OkHttpClient) {
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _incomingState = MutableSharedFlow<List<Agent>>(replay = 1)
    val incomingState = _incomingState.asSharedFlow()

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val remoteAgents = gson.fromJson(text, Array<Agent>::class.java).toList()
                    _incomingState.tryEmit(remoteAgents)
                } catch (e: Exception) {}
            }
        })
    }

    /**
     * Mengirimkan snapshot state lokal ke jaringan (Cloud MySQL bridge).
     */
    fun broadcastLocalState(agents: List<Agent>) {
        if (agents.isNotEmpty()) {
            webSocket?.send(gson.toJson(agents))
        }
    }

    fun broadcastCommand(squadId: Long, formation: String, targetX: Float, targetY: Float) {
        val payload = mapOf(
            "type" to "STRATEGIC_SHIFT",
            "squadId" to squadId,
            "formation" to formation,
            "target" to Pair(targetX, targetY)
        )
        webSocket?.send(gson.toJson(payload))
    }

    fun disconnect() {
        webSocket?.close(1000, "User shutdown")
    }
}
