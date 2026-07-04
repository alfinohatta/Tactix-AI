package com.example.tactixai.core.registry

import com.example.tactixai.core.model.FormationPurpose
import com.example.tactixai.core.model.FormationType

/**
 * Registry Metadata untuk 100 Formasi Perang.
 * Digunakan oleh AI untuk menentukan 'Transition Rules'.
 */
object FormationTaxonomy {

    data class Metadata(
        val type: FormationType,
        val purpose: FormationPurpose,
        val speedMod: Float,       // Pengaruh terhadap kecepatan kelompok
        val defenseMod: Float,     // Bonus pertahanan
        val visibilityMod: Float,  // Stealth multiplier
        val informationCost: Float // Biaya aktivasi dalam EconomyEngine
    )

    private val registry = mutableMapOf<FormationType, Metadata>()

    init {
        // A. OFFENSIVE (High Speed, High Cost)
        register(Metadata(FormationType.SPEARHEAD, FormationPurpose.ATTACK, 1.2f, 0.5f, 1.0f, 15f))
        register(Metadata(FormationType.NEEDLE, FormationPurpose.ATTACK, 1.5f, 0.2f, 0.8f, 20f))
        
        // B. DEFENSIVE (Low Speed, High Defense)
        register(Metadata(FormationType.TURTLE, FormationPurpose.DEFENSE, 0.3f, 2.0f, 1.0f, 25f))
        register(Metadata(FormationType.SHIELD_WALL, FormationPurpose.DEFENSE, 0.5f, 1.5f, 1.0f, 10f))
        
        // C. STEALTH (High Stealth)
        register(Metadata(FormationType.GHOST, FormationPurpose.STEALTH, 1.0f, 0.5f, 0.1f, 30f))
        
        // J. EXPERIMENTAL (High Information Cost)
        register(Metadata(FormationType.BLACK_HOLE, FormationPurpose.EXPERIMENTAL, 0.1f, 3.0f, 2.0f, 100f))
        
        // ... (Mapping untuk 100 formasi lainnya mengikuti kategori besar)
    }

    private fun register(m: Metadata) { registry[m.type] = m }

    fun getMetadata(type: FormationType): Metadata = registry[type] ?: Metadata(type, FormationPurpose.CONTROL, 1f, 1f, 1f, 5f)
}
