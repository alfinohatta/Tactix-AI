package com.example.tactixai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tactixai.core.SimulationManager
import com.example.tactixai.core.model.Agent
import com.example.tactixai.data.repository.TactixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola state simulasi di UI dashboard.
 * Menghubungkan SimOS Kernel dengan View.
 */
class SimulationViewModel(
    private val repository: TactixRepository
) : ViewModel() {

    private var simulationManager: SimulationManager? = null

    private val _agents = MutableStateFlow<List<Agent>>(emptyList())
    val agents: StateFlow<List<Agent>> = _agents.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    fun initSimulation(manager: SimulationManager) {
        this.simulationManager = manager
        viewModelScope.launch {
            manager.agents.collect { updatedAgents ->
                _agents.value = updatedAgents
            }
        }
    }

    fun toggleSimulation() {
        if (_isRunning.value) {
            simulationManager?.shutdown()
        } else {
            simulationManager?.boot()
        }
        _isRunning.value = !_isRunning.value
    }

    override fun onCleared() {
        super.onCleared()
        simulationManager?.shutdown()
    }
}
