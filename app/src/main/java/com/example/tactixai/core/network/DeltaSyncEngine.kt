package com.example.tactixai.core.network

import com.example.tactixai.core.engine.AgentStateBuffer
import com.example.tactixai.core.model.AgentStatus
import org.json.JSONArray
import org.json.JSONObject

/**
 * Mengelola sinkronisasi efisien untuk 10.000 agen.
 * Hanya mengirimkan 'Delta' (perubahan) ke MySQL server untuk menghemat bandwidth.
 */
class DeltaSyncEngine {

    private val lastHealthSnapshot = IntArray(15000)
    private val lastActiveSnapshot = BooleanArray(15000)

    /**
     * Mengidentifikasi agen mana yang mengalami perubahan signifikan.
     */
    fun calculateDelta(buffer: AgentStateBuffer): String? {
        val deltaArray = JSONArray()

        for (i in 0 until buffer.capacity) {
            if (!buffer.active[i] && !lastActiveSnapshot[i]) continue

            // Deteksi perubahan: Mati, Terluka, atau Aktif kembali
            if (buffer.health[i] != lastHealthSnapshot[i] || buffer.active[i] != lastActiveSnapshot[i]) {
                val agentDelta = JSONObject()
                agentDelta.put("id", i)
                agentDelta.put("h", buffer.health[i])
                agentDelta.put("a", buffer.active[i])
                
                // Jika mati, kirim event penghancuran
                if (!buffer.active[i] && lastActiveSnapshot[i]) {
                    agentDelta.put("e", "DESTROYED")
                }

                deltaArray.put(agentDelta)
                
                // Update snapshot
                lastHealthSnapshot[i] = buffer.health[i]
                lastActiveSnapshot[i] = buffer.active[i]
            }
        }

        return if (deltaArray.length() > 0) deltaArray.toString() else null
    }
}
