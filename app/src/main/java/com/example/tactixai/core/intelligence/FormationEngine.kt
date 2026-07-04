package com.example.tactixai.core.intelligence

import com.example.tactixai.core.model.FormationType
import kotlin.math.*

/**
 * Ultimate Formation Intelligence Engine (v6).
 * Memetakan 100+ tipe formasi perang ke algoritma geometri parametrik.
 * Mendukung optimasi skala 10.000+ agen dengan akurasi koordinat Z (3D ready).
 */
class FormationEngine {

    fun getRelativePosition(
        type: FormationType,
        index: Int,
        totalUnits: Int,
        spacing: Float = 35f
    ): Triple<Float, Float, Float> {
        return when (type) {
            // --- PATTERN: OFFENSIVE & BLADED (Wedges, Spears, Blades) ---
            FormationType.SPEARHEAD, FormationType.WEDGE, FormationType.NEEDLE, 
            FormationType.ARMORED_SPEAR, FormationType.TORPEDO_SPEAR, FormationType.HAMMER,
            FormationType.AXE, FormationType.BLADE, FormationType.SILENT_ARROW,
            FormationType.METEOR, FormationType.THREE_ARMY_SPEAR -> 
                calculateWedge(index, spacing, depthMult = 2.0f)
            
            FormationType.FLYING_V, FormationType.YAN_ZHEN, FormationType.TRIANGLE_ASSAULT -> 
                calculateWedge(index, spacing, depthMult = 0.5f)

            // --- PATTERN: DEFENSIVE & ANCIENT (Blocks, Walls, Grids) ---
            FormationType.FANG_ZHEN, FormationType.SHIELD_WALL, FormationType.TURTLE, 
            FormationType.GRID_DEFENSE, FormationType.URBAN_GRID, FormationType.YU_LIN, 
            FormationType.BASTION, FormationType.ANCHOR, FormationType.MOBILE_FORTRESS,
            FormationType.CONVOY, FormationType.TANK_COLUMN, FormationType.FOREST_LINE,
            FormationType.CORAL, FormationType.MULTI_LAYER_SWARM -> 
                calculateGrid(index, totalUnits, spacing)

            // --- PATTERN: CIRCULAR & ORBITAL (Control, Protection, Encirclement) ---
            FormationType.YUAN_ZHEN, FormationType.FORTRESS_RING, FormationType.BUBBLE_DEFENSE,
            FormationType.ORBIT, FormationType.ORBIT_EXP, FormationType.OCEAN_RING, 
            FormationType.BLACK_HOLE, FormationType.DIAMOND, FormationType.STAR_DEFENSE, 
            FormationType.STAR_CLUSTER, FormationType.INVISIBLE_RING, 
            FormationType.AUTONOMOUS_RING, FormationType.DEEP_CIRCLE, FormationType.BA_GUA -> 
                calculateRing(index, totalUnits, radius = spacing * (totalUnits / 5f).coerceAtLeast(5f))

            // --- PATTERN: SWARM & CLOUD (Drone Swarm, Hive Mind, Futuristic) ---
            FormationType.SWARM_CLOUD, FormationType.CLUSTER, FormationType.BEE_SWARM, 
            FormationType.PHANTOM_SWARM, FormationType.GALAXY, FormationType.NEURAL_SWARM,
            FormationType.QUANTUM_GRID, FormationType.HIVE_MIND, FormationType.FRACTAL, 
            FormationType.LIQUID, FormationType.DYNAMIC_MESH, FormationType.EVOLUTION,
            FormationType.PHOENIX, FormationType.ANT_COLONY, FormationType.MOSQUITO -> 
                calculateSwarm(index, totalUnits, spacing)

            // --- PATTERN: CURVE & PINCER (Encirclement, Pursuit) ---
            FormationType.PINCER, FormationType.HE_ZHEN, FormationType.YUE_ZHEN, 
            FormationType.CRAB, FormationType.TRAP, FormationType.WHALE, FormationType.SHARK,
            FormationType.WOLF_PACK -> 
                calculateCrescent(index, totalUnits, spacing)

            // --- PATTERN: VERTICAL & AIR (Aerial Spear, Falcon Dive, Sky Wall) ---
            FormationType.AERIAL_SPEAR, FormationType.SKY_WALL, FormationType.FALCON_DIVE, 
            FormationType.SKY_SHIELD, FormationType.SKY_ANVIL, FormationType.ARROW_RAIN -> 
                calculateVerticalStack(index, totalUnits, spacing)

            else -> calculateGrid(index, totalUnits, spacing)
        }
    }

    private fun calculateWedge(index: Int, spacing: Float, depthMult: Float): Triple<Float, Float, Float> {
        val row = sqrt(index.toDouble() * 2 + 0.25).toInt()
        val col = index - (row * (row - 1) / 2)
        return Triple((col - row / 2f) * spacing, -row * spacing * depthMult, 0f)
    }

    private fun calculateGrid(index: Int, total: Int, spacing: Float): Triple<Float, Float, Float> {
        val side = sqrt(total.toDouble()).toInt().coerceAtLeast(1)
        return Triple((index % side - side / 2f) * spacing, (index / side - side / 2f) * spacing, 0f)
    }

    private fun calculateRing(index: Int, total: Int, radius: Float): Triple<Float, Float, Float> {
        val angle = (2 * PI * index / total).toFloat()
        return Triple(radius * cos(angle), radius * sin(angle), 0f)
    }

    private fun calculateCrescent(index: Int, total: Int, spacing: Float): Triple<Float, Float, Float> {
        val angle = (PI * index / (total - 1) - PI / 2).toFloat()
        val radius = spacing * (total / 3f).coerceAtLeast(6f)
        return Triple(radius * cos(angle), radius * sin(angle) * 0.5f, 0f)
    }

    private fun calculateSwarm(index: Int, total: Int, spacing: Float): Triple<Float, Float, Float> {
        val r = java.util.Random(index.toLong())
        val spread = sqrt(total.toFloat()) * spacing * 1.8f
        return Triple((r.nextFloat() - 0.5f) * spread, (r.nextFloat() - 0.5f) * spread, (r.nextFloat() - 0.5f) * spread)
    }

    private fun calculateVerticalStack(index: Int, total: Int, spacing: Float): Triple<Float, Float, Float> {
        val layer = index / 12
        val posInLayer = index % 12
        val angle = (2 * PI * posInLayer / 12).toFloat()
        return Triple(spacing * cos(angle), spacing * sin(angle), layer * spacing * -1.5f)
    }
}
