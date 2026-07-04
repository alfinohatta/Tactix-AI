package com.example.tactixai.core.registry

import com.example.tactixai.core.intelligence.Doctrine
import com.example.tactixai.core.model.FormationType
import java.util.concurrent.ConcurrentHashMap

/**
 * Layer 14: AI Marketplace Registry.
 * Fondasi untuk ekosistem di mana user dapat berbagi doktrin dan formasi.
 * Mendukung visi B2B untuk pengujian robotika dan sistem otonom.
 */
object MarketplaceRegistry {
    
    private val doctrineVault = ConcurrentHashMap<String, Doctrine>()
    private val formationVault = ConcurrentHashMap<String, (Int, Int, Float) -> Triple<Float, Float, Float>>()

    /**
     * Mendaftarkan Doktrin AI (Otak Komandan) ke dalam platform.
     */
    fun registerDoctrine(id: String, doctrine: Doctrine) {
        doctrineVault[id] = doctrine
    }

    /**
     * Mendaftarkan Algoritma Formasi baru (Marketplace item).
     */
    fun registerFormation(id: String, algorithm: (Int, Int, Float) -> Triple<Float, Float, Float>) {
        formationVault[id] = algorithm
    }

    fun getDoctrine(id: String): Doctrine? = doctrineVault[id]
    
    fun getFormation(id: String) = formationVault[id]

    /**
     * Mengekspor katalog kapabilitas untuk integrasi pihak ketiga.
     */
    fun getPlatformManifest(): List<String> = doctrineVault.keys().toList()
}
