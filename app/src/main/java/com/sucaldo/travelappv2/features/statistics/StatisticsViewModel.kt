package com.sucaldo.travelappv2.features.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper

    init {
        myDb = DatabaseHelper(application.applicationContext)
    }
}