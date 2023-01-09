package com.sucaldo.travelappv2.features.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.Trip
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
            var trips: List<Trip>? = it.trips
            if (it.year == year) {
                trips = getTripsListOfYear(year = it.year, showTrips = it.trips == null)
            }
            TripYear(year = it.year, trips = trips)
        }
        updateTripYears()
    }

    private fun updateTripYears() {
        _uiState.update { it.copy(tripYears = tripYears) }
    }

    private fun getTripsListOfYear(year: Int, showTrips: Boolean): List<Trip>? {
        return if (showTrips) {
            myDb.getTripsOfYearSortedByDate(year)
        } else {
            null
        }
    }

    fun openTripDetails(trip: Trip) {
        _uiState.update { it.copy(showTripDetails = true) }
    }

    fun closeTripDetails() {
        _uiState.update { it.copy(showTripDetails = false) }

    }
}
