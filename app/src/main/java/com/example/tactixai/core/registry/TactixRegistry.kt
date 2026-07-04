package com.example.tactixai.core.registry

import com.example.tactixai.core.intelligence.Doctrine
import com.example.tactixai.core.model.FormationType
import java.util.concurrent.ConcurrentHashMap

/**
 * Layer 14: AI Marketplace & Registry.
 * Tempat pendaftaran Doktrin dan Formasi kustom.
 * Memungkinkan platform untuk di-expand oleh komunitas/pengembang lain.
 */
object TactixRegistry {
    private val customDoctrines = ConcurrentHashMap<String, Doctrine>()
    private val customFormations = ConcurrentHashMap<String, (Int, Int, Float) -> Pair<Float, Float>>()

    fun registerDoctrine(name: String, doctrine: Doctrine) {
        customDoctrines[name] = doctrine
    }

    fun registerFormation(name: String, calculator: (Int, Int, Float) -> Pair<Float, Float>) {
        customFormations[name] = calculator
    }

    fun getDoctrine(name: String): Doctrine? = customDoctrines[name]
    
    fun getCustomFormation(name: String, index: Int, total: Int, spacing: Float): Pair<Float, Float>? {
        return customFormations[name]?.invoke(index, total, spacing)
    }
}
