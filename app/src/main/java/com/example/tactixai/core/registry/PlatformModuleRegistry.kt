package com.example.tactixai.core.registry

import com.example.tactixai.core.intelligence.Doctrine
import com.example.tactixai.core.model.FormationType
import java.util.concurrent.ConcurrentHashMap

/**
 * Layer 14 & 15: AI Marketplace & Company Vision Expansion.
 * Registry pusat untuk kapabilitas platform.
 * Memungkinkan platform dikonfigurasi untuk Militer, Logistik, atau Robotika.
 */
object PlatformModuleRegistry {
    
    private val doctrineLibrary = ConcurrentHashMap<String, Doctrine>()
    private val formationAlgorithms = ConcurrentHashMap<FormationType, (Int, Int, Float) -> Triple<Float, Float, Float>>()

    /**
     * Mendaftarkan Doktrin AI baru ke dalam OS.
     */
    fun registerDoctrine(id: String, doctrine: Doctrine) {
        doctrineLibrary[id] = doctrine
    }

    /**
     * Mendaftarkan Algoritma Formasi kustom.
     */
    fun registerFormation(type: FormationType, algorithm: (Int, Int, Float) -> Triple<Float, Float, Float>) {
        formationAlgorithms[type] = algorithm
    }

    fun getDoctrine(id: String): Doctrine? = doctrineLibrary[id]
    
    fun getFormationAlgorithm(type: FormationType) = formationAlgorithms[type]

    /**
     * Mendapatkan daftar modul yang tersedia untuk Marketplace.
     */
    fun getAvailableModules(): List<String> = doctrineLibrary.keys().toList()
}
