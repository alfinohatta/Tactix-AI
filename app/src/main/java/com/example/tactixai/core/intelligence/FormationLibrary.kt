package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.FormationPurpose
import com.example.tactixai.core.model.FormationType

/**
 * Metadata Strategis untuk 100 Formasi Perang.
 * Bertindak sebagai 'Brain Knowledge Base' untuk SimOS.
 * Memetakan efektivitas taktis untuk pengoptimalan otonom (Startup-Grade).
 */
object FormationLibrary {

    data class FormationStats(
        val type: FormationType,
        val purpose: FormationPurpose,
        val offensivePower: Float, // 0.0 - 1.0
        val defensivePower: Float,
        val mobility: Float,
        val stealthLevel: Float,
        val complexity: Int // Biaya 'Information' untuk aktivasi otonom
    )

    private val library = mutableMapOf<FormationType, FormationStats>()

    init {
        // A. OFFENSIVE (1-10) - Fokus pada Penetrasi dan Daya Hancur
        register(FormationStats(FormationType.SPEARHEAD, FormationPurpose.ATTACK, 0.9f, 0.3f, 0.8f, 0.1f, 10))
        register(FormationStats(FormationType.WEDGE, FormationPurpose.ATTACK, 0.85f, 0.4f, 0.7f, 0.1f, 12))
        register(FormationStats(FormationType.FLYING_V, FormationPurpose.ATTACK, 0.75f, 0.3f, 0.9f, 0.2f, 15))
        register(FormationStats(FormationType.DIAMOND, FormationPurpose.ATTACK, 0.7f, 0.6f, 0.7f, 0.3f, 18))
        register(FormationStats(FormationType.TRIANGLE_ASSAULT, FormationPurpose.ATTACK, 0.8f, 0.4f, 0.85f, 0.2f, 14))
        register(FormationStats(FormationType.NEEDLE, FormationPurpose.ATTACK, 1.0f, 0.1f, 0.95f, 0.1f, 20))
        register(FormationStats(FormationType.HAMMER, FormationPurpose.ATTACK, 0.95f, 0.5f, 0.4f, 0.0f, 25))
        register(FormationStats(FormationType.AXE, FormationPurpose.ATTACK, 0.9f, 0.4f, 0.6f, 0.1f, 22))
        register(FormationStats(FormationType.BLADE, FormationPurpose.ATTACK, 0.85f, 0.2f, 0.95f, 0.4f, 16))
        register(FormationStats(FormationType.ARROW_RAIN, FormationPurpose.ATTACK, 0.8f, 0.2f, 0.5f, 0.1f, 30))

        // B. ANCIENT CHINA ADAPTATION (11-20) - Fokus pada Keseimbangan dan Disiplin
        register(FormationStats(FormationType.FANG_ZHEN, FormationPurpose.DEFENSE, 0.5f, 0.8f, 0.4f, 0.1f, 12))
        register(FormationStats(FormationType.YU_LIN, FormationPurpose.ATTACK, 0.7f, 0.6f, 0.5f, 0.2f, 15))
        register(FormationStats(FormationType.YAN_ZHEN, FormationPurpose.RECON, 0.4f, 0.4f, 0.8f, 0.5f, 10))
        register(FormationStats(FormationType.HE_ZHEN, FormationPurpose.CONTROL, 0.6f, 0.7f, 0.5f, 0.2f, 20))
        register(FormationStats(FormationType.CHANG_SHE, FormationPurpose.RECON, 0.3f, 0.3f, 1.0f, 0.6f, 8))
        register(FormationStats(FormationType.BA_GUA, FormationPurpose.DEFENSE, 0.4f, 0.95f, 0.3f, 0.4f, 40))
        register(FormationStats(FormationType.YUAN_ZHEN, FormationPurpose.DEFENSE, 0.2f, 1.0f, 0.2f, 0.1f, 25))
        register(FormationStats(FormationType.YUE_ZHEN, FormationPurpose.CONTROL, 0.65f, 0.5f, 0.7f, 0.3f, 18))
        register(FormationStats(FormationType.LONG_ZHEN, FormationPurpose.EXPERIMENTAL, 0.75f, 0.75f, 0.75f, 0.75f, 50))
        register(FormationStats(FormationType.TIAN_DI_REN, FormationPurpose.EXPERIMENTAL, 0.9f, 0.9f, 0.9f, 0.9f, 60))

        // C. DRONE SWARM (21-30) - Fokus pada Mobilitas dan Gangguan Sensor
        register(FormationStats(FormationType.SWARM_CLOUD, FormationPurpose.CONTROL, 0.5f, 0.3f, 1.0f, 0.5f, 15))
        register(FormationStats(FormationType.CLUSTER, FormationPurpose.ATTACK, 0.7f, 0.4f, 0.8f, 0.3f, 18))
        register(FormationStats(FormationType.BEE_SWARM, FormationPurpose.ATTACK, 0.9f, 0.2f, 0.95f, 0.2f, 25))
        register(FormationStats(FormationType.FALCON_DIVE, FormationPurpose.ATTACK, 1.0f, 0.1f, 1.0f, 0.3f, 22))
        register(FormationStats(FormationType.EAGLE, FormationPurpose.RECON, 0.3f, 0.2f, 0.9f, 0.8f, 12))
        register(FormationStats(FormationType.OWL, FormationPurpose.STEALTH, 0.4f, 0.2f, 0.7f, 1.0f, 20))
        register(FormationStats(FormationType.BAT, FormationPurpose.STEALTH, 0.3f, 0.2f, 0.8f, 0.95f, 18))
        register(FormationStats(FormationType.MOSQUITO, FormationPurpose.STEALTH, 0.2f, 0.1f, 1.0f, 1.0f, 35))
        register(FormationStats(FormationType.VORTEX, FormationPurpose.CONTROL, 0.6f, 0.6f, 0.8f, 0.4f, 28))
        register(FormationStats(FormationType.SPIRAL, FormationPurpose.CONTROL, 0.5f, 0.5f, 0.9f, 0.5f, 25))

        // D. DEFENSIVE (31-40) - Fokus pada Daya Tahan Maksimal
        register(FormationStats(FormationType.SHIELD_WALL, FormationPurpose.DEFENSE, 0.2f, 0.9f, 0.3f, 0.1f, 15))
        register(FormationStats(FormationType.TURTLE, FormationPurpose.DEFENSE, 0.1f, 1.0f, 0.1f, 0.0f, 30))
        register(FormationStats(FormationType.FORTRESS_RING, FormationPurpose.DEFENSE, 0.3f, 0.95f, 0.2f, 0.0f, 28))
        register(FormationStats(FormationType.LAYER_DEFENSE, FormationPurpose.DEFENSE, 0.4f, 0.85f, 0.4f, 0.1f, 35))
        register(FormationStats(FormationType.BUBBLE_DEFENSE, FormationPurpose.DEFENSE, 0.1f, 0.9f, 0.5f, 0.3f, 40))
        register(FormationStats(FormationType.GRID_DEFENSE, FormationPurpose.CONTROL, 0.3f, 0.7f, 0.5f, 0.6f, 20))
        register(FormationStats(FormationType.MESH_DEFENSE, FormationPurpose.CONTROL, 0.4f, 0.6f, 0.8f, 0.5f, 25))
        register(FormationStats(FormationType.STAR_DEFENSE, FormationPurpose.DEFENSE, 0.5f, 0.8f, 0.4f, 0.2f, 22))
        register(FormationStats(FormationType.BASTION, FormationPurpose.DEFENSE, 0.4f, 0.9f, 0.2f, 0.0f, 30))
        register(FormationStats(FormationType.ANCHOR, FormationPurpose.DEFENSE, 0.2f, 0.95f, 0.1f, 0.1f, 15))

        // E. SUBMARINE / SEA (41-50) - Fokus pada Deteksi dan Serangan Bawah Air
        register(FormationStats(FormationType.WOLF_PACK, FormationPurpose.ATTACK, 0.85f, 0.4f, 0.7f, 0.6f, 30))
        register(FormationStats(FormationType.DEEP_CIRCLE, FormationPurpose.DEFENSE, 0.2f, 0.8f, 0.3f, 0.7f, 25))
        register(FormationStats(FormationType.SILENT_NET, FormationPurpose.RECON, 0.1f, 0.3f, 0.6f, 1.0f, 40))
        register(FormationStats(FormationType.ABYSS_LINE, FormationPurpose.RECON, 0.2f, 0.4f, 0.8f, 0.9f, 20))
        register(FormationStats(FormationType.TRIDENT, FormationPurpose.ATTACK, 0.9f, 0.4f, 0.7f, 0.5f, 35))
        register(FormationStats(FormationType.SHARK, FormationPurpose.ATTACK, 0.95f, 0.3f, 0.8f, 0.6f, 28))
        register(FormationStats(FormationType.WHALE, FormationPurpose.ESCORT, 0.4f, 0.9f, 0.5f, 0.4f, 45))
        register(FormationStats(FormationType.CORAL, FormationPurpose.DEFENSE, 0.2f, 0.8f, 0.1f, 0.8f, 15))
        register(FormationStats(FormationType.TORPEDO_SPEAR, FormationPurpose.ATTACK, 1.0f, 0.2f, 0.9f, 0.4f, 50))
        register(FormationStats(FormationType.OCEAN_RING, FormationPurpose.CONTROL, 0.4f, 0.7f, 0.6f, 0.5f, 30))

        // F. GROUND ROBOT (51-60) - Fokus pada Formasi Medan Darat
        register(FormationStats(FormationType.TANK_COLUMN, FormationPurpose.ATTACK, 0.8f, 0.7f, 0.5f, 0.0f, 20))
        register(FormationStats(FormationType.CONVOY, FormationPurpose.ESCORT, 0.4f, 0.6f, 0.7f, 0.1f, 15))
        register(FormationStats(FormationType.URBAN_GRID, FormationPurpose.CONTROL, 0.6f, 0.7f, 0.4f, 0.3f, 25))
        register(FormationStats(FormationType.FOREST_LINE, FormationPurpose.STEALTH, 0.4f, 0.5f, 0.6f, 0.8f, 18))
        register(FormationStats(FormationType.MOUNTAIN_V, FormationPurpose.ATTACK, 0.75f, 0.6f, 0.4f, 0.5f, 22))
        register(FormationStats(FormationType.CRAWLER, FormationPurpose.STEALTH, 0.3f, 0.4f, 0.5f, 0.9f, 25))
        register(FormationStats(FormationType.SPIDER, FormationPurpose.CONTROL, 0.6f, 0.6f, 0.8f, 0.6f, 30))
        register(FormationStats(FormationType.CRAB, FormationPurpose.CONTROL, 0.5f, 0.8f, 0.6f, 0.4f, 24))
        register(FormationStats(FormationType.ANT_COLONY, FormationPurpose.CONTROL, 0.4f, 0.4f, 0.9f, 0.5f, 20))
        register(FormationStats(FormationType.PACK, FormationPurpose.ATTACK, 0.85f, 0.5f, 0.85f, 0.4f, 28))

        // G. STEALTH / INTEL (61-70) - Fokus pada Infiltrasi
        register(FormationStats(FormationType.GHOST, FormationPurpose.STEALTH, 0.4f, 0.2f, 0.9f, 1.0f, 40))
        register(FormationStats(FormationType.SHADOW, FormationPurpose.STEALTH, 0.3f, 0.3f, 0.8f, 0.95f, 35))
        register(FormationStats(FormationType.MIRROR, FormationPurpose.STEALTH, 0.5f, 0.5f, 0.7f, 0.9f, 45))
        register(FormationStats(FormationType.DECOY, FormationPurpose.EXPERIMENTAL, 0.2f, 0.2f, 0.8f, 0.8f, 20))
        register(FormationStats(FormationType.PHANTOM_SWARM, FormationPurpose.STEALTH, 0.6f, 0.3f, 1.0f, 0.9f, 50))
        register(FormationStats(FormationType.INVISIBLE_RING, FormationPurpose.STEALTH, 0.2f, 0.6f, 0.4f, 1.0f, 45))
        register(FormationStats(FormationType.SILENT_ARROW, FormationPurpose.ATTACK, 0.9f, 0.2f, 0.9f, 0.8f, 55))
        register(FormationStats(FormationType.AMBUSH, FormationPurpose.ATTACK, 1.0f, 0.4f, 0.2f, 1.0f, 30))
        register(FormationStats(FormationType.TRAP, FormationPurpose.CONTROL, 0.7f, 0.6f, 0.3f, 0.9f, 35))
        register(FormationStats(FormationType.BAIT, FormationPurpose.EXPERIMENTAL, 0.1f, 0.2f, 0.9f, 0.5f, 15))

        // H. FUTURISTIC AI (71-80) - High Complexity / Intelligence Focus
        register(FormationStats(FormationType.NEURAL_SWARM, FormationPurpose.ATTACK, 0.9f, 0.8f, 0.95f, 0.6f, 70))
        register(FormationStats(FormationType.ADAPTIVE, FormationPurpose.EXPERIMENTAL, 0.85f, 0.85f, 0.85f, 0.85f, 80))
        register(FormationStats(FormationType.SELF_HEALING, FormationPurpose.DEFENSE, 0.4f, 1.0f, 0.6f, 0.3f, 90))
        register(FormationStats(FormationType.QUANTUM_GRID, FormationPurpose.CONTROL, 0.7f, 0.7f, 1.0f, 0.9f, 75))
        register(FormationStats(FormationType.HIVE_MIND, FormationPurpose.EXPERIMENTAL, 0.95f, 0.95f, 0.95f, 0.7f, 100))
        register(FormationStats(FormationType.FRACTAL, FormationPurpose.CONTROL, 0.6f, 0.8f, 0.7f, 0.6f, 65))
        register(FormationStats(FormationType.LIQUID, FormationPurpose.EXPERIMENTAL, 0.7f, 0.7f, 1.0f, 0.8f, 85))
        register(FormationStats(FormationType.DYNAMIC_MESH, FormationPurpose.CONTROL, 0.5f, 0.5f, 1.0f, 0.7f, 50))
        register(FormationStats(FormationType.AUTONOMOUS_RING, FormationPurpose.DEFENSE, 0.3f, 0.9f, 0.6f, 0.5f, 60))
        register(FormationStats(FormationType.EVOLUTION, FormationPurpose.EXPERIMENTAL, 0.8f, 0.8f, 0.8f, 0.8f, 120))

        // I. COMBINATION (81-90) - Multi-Domain Synergy
        register(FormationStats(FormationType.AIR_LAND_SEA, FormationPurpose.CONTROL, 0.9f, 0.9f, 0.7f, 0.5f, 100))
        register(FormationStats(FormationType.THREE_ARMY_SPEAR, FormationPurpose.ATTACK, 1.0f, 0.6f, 0.8f, 0.3f, 110))
        register(FormationStats(FormationType.SKY_SHIELD, FormationPurpose.DEFENSE, 0.3f, 0.95f, 0.6f, 0.2f, 80))
        register(FormationStats(FormationType.OCEAN_WALL, FormationPurpose.DEFENSE, 0.2f, 0.9f, 0.4f, 0.1f, 70))
        register(FormationStats(FormationType.LAND_HAMMER, FormationPurpose.ATTACK, 0.95f, 0.7f, 0.5f, 0.0f, 75))
        register(FormationStats(FormationType.SKY_ANVIL, FormationPurpose.CONTROL, 0.7f, 0.8f, 0.6f, 0.4f, 85))
        register(FormationStats(FormationType.MULTI_LAYER_SWARM, FormationPurpose.ATTACK, 0.9f, 0.7f, 0.9f, 0.5f, 95))
        register(FormationStats(FormationType.DRAGON_NETWORK, FormationPurpose.EXPERIMENTAL, 0.85f, 0.85f, 1.0f, 0.6f, 130))
        register(FormationStats(FormationType.PHOENIX, FormationPurpose.EXPERIMENTAL, 0.8f, 0.8f, 0.8f, 0.8f, 150))
        register(FormationStats(FormationType.TITAN, FormationPurpose.ATTACK, 1.0f, 1.0f, 0.3f, 0.0f, 140))

        // J. EXPERIMENTAL (91-100) - Paradigma Simulasi Baru
        register(FormationStats(FormationType.TIME_DELAY, FormationPurpose.EXPERIMENTAL, 0.7f, 0.6f, 0.8f, 0.4f, 60))
        register(FormationStats(FormationType.WAVE, FormationPurpose.ATTACK, 0.85f, 0.5f, 0.75f, 0.2f, 55))
        register(FormationStats(FormationType.TSUNAMI, FormationPurpose.ATTACK, 1.0f, 0.3f, 0.6f, 0.0f, 80))
        register(FormationStats(FormationType.METEOR, FormationPurpose.ATTACK, 1.0f, 0.1f, 1.0f, 0.1f, 90))
        register(FormationStats(FormationType.STAR_CLUSTER, FormationPurpose.CONTROL, 0.5f, 0.5f, 0.9f, 0.6f, 45))
        register(FormationStats(FormationType.GALAXY, FormationPurpose.CONTROL, 0.6f, 0.6f, 0.8f, 0.5f, 70))
        register(FormationStats(FormationType.ORBIT_EXP, FormationPurpose.CONTROL, 0.4f, 0.8f, 0.7f, 0.4f, 65))
        register(FormationStats(FormationType.GRAVITY_WELL, FormationPurpose.CONTROL, 0.8f, 0.8f, 0.2f, 0.1f, 100))
        register(FormationStats(FormationType.BLACK_HOLE, FormationPurpose.CONTROL, 1.0f, 1.0f, 0.1f, 0.0f, 200))
        register(FormationStats(FormationType.OMEGA, FormationPurpose.EXPERIMENTAL, 1.0f, 1.0f, 1.0f, 1.0f, 500))
    }

    private fun register(stats: FormationStats) {
        library[stats.type] = stats
    }

    fun getStats(type: FormationType): FormationStats? = library[type]

    fun findBest(purpose: FormationPurpose, minMobility: Float = 0f): FormationType {
        return library.values
            .filter { it.purpose == purpose && it.mobility >= minMobility }
            .maxByOrNull { it.offensivePower + it.defensivePower }?.type ?: FormationType.ADAPTIVE
    }

    fun listAllFormations(): List<FormationStats> = library.values.toList()
}
