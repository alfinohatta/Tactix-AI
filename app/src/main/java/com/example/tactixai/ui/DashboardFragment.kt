package com.example.tactixai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tactixai.R
import com.example.tactixai.core.SimulationManager
import com.example.tactixai.ui.renderer.TactixSimulationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Dashboard Utama Tactix AI SimOS.
 * Menampilkan visualisasi 10k agen, Heatmap, dan Narrative AI secara reaktif.
 */
class DashboardFragment : Fragment() {

    private var simulationView: TactixSimulationView? = null
    private var aiLogText: TextView? = null
    private var statsText: TextView? = null
    private var simulationManager: SimulationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        simulationView = view.findViewById(R.id.simulationView)
        aiLogText = view.findViewById(R.id.aiLogText)
        statsText = view.findViewById(R.id.statsText)
        
        simulationManager?.let { observeSimulation(it) }
    }

    /**
     * Menyuntikkan SimulationManager dan memulai rantai observasi SimOS.
     */
    fun setManager(manager: SimulationManager) {
        this.simulationManager = manager
        if (isAdded) {
            observeSimulation(manager)
        }
    }

    private fun observeSimulation(manager: SimulationManager) {
        // 1. Observasi Jantung Mekanis (Primitive Buffer - DOD Optimization)
        // Kita tidak lagi mengobservasi StateFlow<List<Agent>> untuk merender 10k unit.
        // Sebaliknya, View membaca langsung dari buffer simulasi setiap frame.
        simulationView?.setSimulationContext(manager.getBuffer(), manager.getInfluenceMap())

        // 2. Observasi Narasi AI (Intelligence Layer - Explainability)
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                val logs = manager.getStrategicLogs()
                if (logs.isNotEmpty()) {
                    val latest = logs.first()
                    aiLogText?.text = "STRATEGIC INTENT:\n${latest.entityName}\n\nREASON: ${latest.reason}\n\nIMPACT: ${latest.impact}"
                }
                delay(1000)
            }
        }
        
        // 3. Observasi Performa & Progress (Analytics Layer)
        viewLifecycleOwner.lifecycleScope.launch {
            while(true) {
                val report = manager.getPerformanceReport()
                val economy = manager.getEconomyStatus()
                statsText?.text = "AGENTS: ${report.combatEffectiveUnits} | SURVIVAL: ${(report.survivalRate * 100).toInt()}% | INFO: ${economy.information.toInt()}/${economy.informationCapacity.toInt()}"
                delay(500)
            }
        }
    }
}
