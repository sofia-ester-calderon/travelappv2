package com.sucaldo.travelappv2.features.citycoordinates

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.db.DatabaseHelper
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
}