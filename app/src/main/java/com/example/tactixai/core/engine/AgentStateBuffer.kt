package com.example.tactixai.core.engine

/**
 * Ultimate DOD State Buffer.
 * Menyimpan seluruh data fisik dan kognitif agen dalam primitive arrays.
 * Kapasitas: 10.000+ Agen.
 */
class AgentStateBuffer(val capacity: Int) {
    // Physics Layer
    val x = FloatArray(capacity)
    val y = FloatArray(capacity)
    val z = FloatArray(capacity)
    val vx = FloatArray(capacity)
    val vy = FloatArray(capacity)
    
    // Resource Layer
    val health = IntArray(capacity)
    val energy = IntArray(capacity)
    val active = BooleanArray(capacity)
    
    // Intelligence Layer (Blueprint Layer 2)
    val commanderId = LongArray(capacity)
    val squadId = LongArray(capacity)
    val domain = IntArray(capacity) // 0: Air, 1: Land, 2: Sea, 3: Underwater
    val role = IntArray(capacity)
    
    // Cognitive State
    val confidence = FloatArray(capacity) // 0.0 to 1.0
    val threatLevel = IntArray(capacity)  // 0: Low, 1: Med, 2: High, 3: Critical
    
    // Target Layer
    val targetX = FloatArray(capacity)
    val targetY = FloatArray(capacity)

    fun deactivateAll() {
        active.fill(false)
    }
    
    fun setAgent(i: Int, posX: Float, posY: Float, dom: Int, commId: Long, sId: Long = 0L) {
        x[i] = posX
        y[i] = posY
        domain[i] = dom
        commanderId[i] = commId
        squadId[i] = sId
        active[i] = true
        health[i] = 100
        energy[i] = 100
        confidence[i] = 1.0f
    }
}
