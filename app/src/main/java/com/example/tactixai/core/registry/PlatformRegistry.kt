package com.example.tactixai.core.registry

import com.example.tactixai.core.intelligence.Doctrine
import com.example.tactixai.core.model.FormationType
import java.util.concurrent.ConcurrentHashMap

/**
 * Layer 14: AI Marketplace Registry.
 * Menjadikan Tactix AI sebagai Platform OS dengan arsitektur Plug-and-Play.
 * Memungkinkan pihak ketiga mendaftarkan Doktrin, Formasi, dan Aturan baru.
 */
object PlatformRegistry {
    
    private val doctrineLibrary = ConcurrentHashMap<String, Doctrine>()
    private val formationLibrary = ConcurrentHashMap<FormationType, (Int, Int, Float) -> Pair<Float, Float>>()

    /**
     * Mendaftarkan doktrin baru ke dalam OS.
     */
    fun registerDoctrine(id: String, doctrine: Doctrine) {
        doctrineLibrary[id] = doctrine
    }

    /**
     * Mendaftarkan algoritma formasi kustom.
     */
    fun registerFormation(type: FormationType, algorithm: (Int, Int, Float) -> Pair<Float, Float>) {
        formationLibrary[type] = algorithm
    }

    fun getDoctrine(id: String): Doctrine? = doctrineLibrary[id]
    
    fun getFormationAlgorithm(type: FormationType) = formationLibrary[type]

    /**
     * Mendapatkan daftar kapabilitas platform untuk B2B/Enterprise.
     */
    fun getAvailableCapabilities(): List<String> = doctrineLibrary.keys().toList()
}
