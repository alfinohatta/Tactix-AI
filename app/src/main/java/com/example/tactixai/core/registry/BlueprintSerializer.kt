package com.example.tactixai.core.registry

import com.example.tactixai.core.model.SimulationBlueprint
import org.json.JSONObject
import org.json.JSONArray

/**
 * Layer 14: Blueprint Serializer.
 * Mengonversi desain ekosistem (SimulationBlueprint) ke format JSON.
 * Kunci untuk fitur AI Marketplace dan interoperabilitas B2B.
 */
object BlueprintSerializer {

    fun serialize(blueprint: SimulationBlueprint): String {
        val json = JSONObject()
        json.put("name", blueprint.name)
        json.put("description", blueprint.description)
        json.put("mapTemplate", blueprint.mapTemplate)
        json.put("agentCount", blueprint.agentCount)
        json.put("initialFormation", blueprint.initialFormation.name)
        json.put("objective", blueprint.objective.name)
        json.put("commanderPersonality", blueprint.commanderPersonality.name)
        json.put("informationBudget", blueprint.informationBudget.toDouble())
        json.put("energyScarcity", blueprint.energyScarcity.toDouble())

        val rulesArray = JSONArray()
        blueprint.rules.forEach { rule ->
            val ruleJson = JSONObject()
            ruleJson.put("type", rule.type)
            ruleJson.put("magnitude", rule.magnitude.toDouble())
            rulesArray.put(ruleJson)
        }
        json.put("rules", rulesArray)

        return json.toString(4)
    }

    /**
     * Membangun Blueprint dari data JSON yang diunduh dari Marketplace.
     */
    fun deserialize(jsonString: String): JSONObject {
        // Dalam implementasi nyata, ini akan mengembalikan objek SimulationBlueprint
        return JSONObject(jsonString)
    }
}
