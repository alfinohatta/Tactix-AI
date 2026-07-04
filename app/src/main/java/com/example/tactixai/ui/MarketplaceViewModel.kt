package com.example.tactixai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tactixai.core.model.SimulationBlueprint
import com.example.tactixai.data.repository.MarketplaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Layer 14: AI Marketplace ViewModel.
 * Mengelola interaksi pengguna dengan Cloud Marketplace.
 */
class MarketplaceViewModel(private val repository: MarketplaceRepository) : ViewModel() {

    private val _availableBlueprints = MutableStateFlow<List<SimulationBlueprint>>(emptyList())
    val availableBlueprints = _availableBlueprints.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    fun refreshMarketplace() {
        viewModelScope.launch {
            val list = repository.fetchGlobalMarketplace()
            _availableBlueprints.value = list
        }
    }

    fun publishBlueprint(blueprint: SimulationBlueprint) {
        _isUploading.value = true
        viewModelScope.launch {
            val success = repository.publishToCloud(blueprint)
            _isUploading.value = false
            if (success) refreshMarketplace()
        }
    }

    fun downloadAndSelect(blueprint: SimulationBlueprint) {
        repository.saveToLocalCache(blueprint)
        // Logic untuk memicu SimOS Kernel boot dengan blueprint ini
    }
}
