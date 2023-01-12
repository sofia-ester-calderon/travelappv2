package com.sucaldo.travelappv2.features.citycoordinates

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.db.DatabaseHelper
import com.sucaldo.travelappv2.util.DistanceCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CityCoordinatesViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CityCoordinatesUiState())
    val uiState: StateFlow<CityCoordinatesUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper

    init {
        myDb = DatabaseHelper(application.applicationContext)
    }

    fun updateCountry(country: String) {
        updateErrorType(FieldErrorType.NONE)
        _uiState.update { it.copy(country = country) }
    }

    fun updateCity(city: String) {
        updateErrorType(FieldErrorType.NONE)
        _uiState.update { it.copy(city = city) }
    }

    fun searchLocation() {
        val country = _uiState.value.country
        val city = _uiState.value.city
        if (country.isBlank() && city.isBlank()) {
            updateErrorType(FieldErrorType.EMPTY)
            return
        }

        val storedCityCoordinates = myDb.getStoredCityCoordinates(country, city)
        if (storedCityCoordinates.isEmpty()) {
            updateErrorType(FieldErrorType.LOCATION_DB)
        }
        _uiState.update { it.copy(cityCoordinates = storedCityCoordinates) }
    }

    private fun updateErrorType(errorType: FieldErrorType) {
        _uiState.update { it.copy(countryErrorType = errorType, cityErrorType = errorType) }
    }

    fun closeDetails() {
        updateSelectedLocation(null)
    }

    fun openDetails(cityLocation: CityLocation) {
        updateSelectedLocation(cityLocation)
    }

    fun updateLatitude(latitude: String) {
        _uiState.value.selectedCityCoordinate?.let {
            val updatedLocation = CityLocation(
                id = it.id,
                country = it.country,
                city = it.city,
                latitude = latitude.toFloat(),
                longitude = it.longitude,
            )
            updateSelectedLocation(updatedLocation)
        }
    }

    fun updateLongitude(longitude: String) {
        _uiState.value.selectedCityCoordinate?.let {
            val updatedLocation = CityLocation(
                id = it.id,
                country = it.country,
                city = it.city,
                latitude = it.latitude,
                longitude = longitude.toFloat(),
            )
            updateSelectedLocation(updatedLocation)
        }
    }

    fun updateCityLocation() {
        _uiState.value.selectedCityCoordinate?.let {
            myDb.updateCityLocation(it)
            val tripsToRecalculate = myDb.getTripsThatContainSpecificLocation(it.country, it.city)
            updateTripDistance(tripsToRecalculate)
            closeDetails()
            searchLocation()
        }
    }

    private fun updateTripDistance(trips: List<Trip>) {
        trips.forEach {
            val fromCityLoc = myDb.getLocationOfCity(it.fromCountry, it.fromCity)
            val toCityLoc = myDb.getLocationOfCity(it.toCountry, it.toCity)
            if (fromCityLoc != null && toCityLoc != null) {
                val distance = DistanceCalculator.getDistanceFromLatLongInKms(
                    fromCityLoc.latitude,
                    fromCityLoc.longitude,
                    toCityLoc.latitude,
                    toCityLoc.longitude,
                )
                it.distance = distance
                myDb.updateTrip(it)
            }

        }
    }

    private fun updateSelectedLocation(location: CityLocation?) {
        _uiState.update { it.copy(selectedCityCoordinate = location) }
    }
}