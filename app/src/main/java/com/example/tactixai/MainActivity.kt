package com.example.tactixai

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tactixai.core.SimulationManager
import com.example.tactixai.core.model.BlueprintFactory
import com.example.tactixai.core.ScenarioFactory
import com.example.tactixai.ui.DashboardFragment
import com.example.tactixai.data.repository.TactixRepository

/**
 * SimOS Bootloader (Startup-Grade).
 * Entry point utama platform Tactix AI.
 * Mengelola siklus hidup ekosistem simulasi otonom.
 */
class MainActivity : AppCompatActivity() {

    private var currentManager: SimulationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Injeksi Dependensi via AppContainer
        val app = application as TactixApplication
        val container = app.container

        // 1. Inisialisasi SimOS Dashboard
        val dashboard = DashboardFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, dashboard)
            .commit()

        // 2. Boot Skenario Default
        bootScenario(dashboard, container.tactixRepository)

        // 3. Control Panel Setup
        findViewById<Button>(R.id.btnStart).setOnClickListener {
            if (currentManager != null) {
                currentManager?.boot()
                Toast.makeText(this, "SimOS Kernel Active: Deploying Agents", Toast.LENGTH_SHORT).show()
            }
        }
        
        findViewById<Button>(R.id.btnCommand).setOnClickListener {
            // Trigger Emergent Maneuver (Fake Attack / Pincer) melalui Manager
            Toast.makeText(this, "Strategic Intent Issued", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bootScenario(
        fragment: DashboardFragment, 
        repository: TactixRepository,
    ) {
        // A. Konfigurasi Blueprint (Objective & Rules)
        val agentCount = 10000
        val blueprint = BlueprintFactory.createIslandInvasion(agents = agentCount)
        
        // B. Generate Scenario (Agents, Squads, Commanders)
        val scenario = ScenarioFactory.createSwarmInvasion(agentCount)
        
        // C. Inisialisasi Orchestrator dengan Agen Nyata
        currentManager = SimulationManager(
            blueprint = blueprint,
            initialAgents = scenario.agents,
            commanders = scenario.commanders,
            squads = scenario.squads,
            repository = repository,
            userPlan = "ENTERPRISE"
        )

        fragment.setManager(currentManager!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentManager?.shutdown()
    }
}
