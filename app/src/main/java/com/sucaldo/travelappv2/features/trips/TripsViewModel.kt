package com.sucaldo.travelappv2.features.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TripsViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(TripsUiState())
    val uiState: StateFlow<TripsUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper
    private var tripYears: List<TripYear>

    init {
        myDb = DatabaseHelper(application.applicationContext)
        val years = myDb.allYearsOfTrips
        tripYears = years.map { TripYear(it) }
        updateTripYears()
    }

    fun onExpandYear(year: Int) {
        tripYears = tripYears.map {
            val expanded = if (it.year == year) {
                !it.expanded
            } else {
                it.expanded
            }
            TripYear(year = it.year, expanded = expanded)
        }
        updateTripYears()
    }

    private fun updateTripYears() {
        _uiState.update { it.copy(tripYears = tripYears) }
    }
}
