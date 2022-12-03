package com.sucaldo.travelappv2.ui.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.TripType
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper

    init {
        myDb = DatabaseHelper(application.applicationContext)
    }

    fun updateTripType(tripType: TripType) {
        _uiState.value = _uiState.value.copy(tripType = tripType)
    }

    fun updateFromCountry(country: String) {
        _uiState.value = _uiState.value.copy(fromCountry = country)
    }

    fun updateFromCity(city: String) {
        _uiState.value = _uiState.value.copy(fromCity = city)
    }

    fun updateFromLatitude(latitude: String) {
        _uiState.value = _uiState.value.copy(fromLatitudeText = latitude, isFromLatLongDbError = false)
    }

    fun updateLongitude(longitude: String) {
        _uiState.value = _uiState.value.copy(fromLongitudeText = longitude, isFromLatLongDbError = false)
    }

    fun onFromCalculateLatLong() {
        _uiState.value = _uiState.value.copy(
            isFromLatLongDbError = false,
            fromLongitudeText = "",
            fromLatitudeText = "",
        )
        val country = uiState.value.fromCountry
        val city = uiState.value.fromCity
        try {
            val cityLocation: CityLocation = myDb.getLocationOfCity(country, city)
            _uiState.value = _uiState.value.copy(
                fromLatitudeText = formatLatLong(cityLocation.latitude)
            )
            _uiState.value = _uiState.value.copy(
                fromLongitudeText = formatLatLong(cityLocation.longitude)
            )
        } catch (e: java.lang.Exception) {
            _uiState.value = uiState.value.copy(isFromLatLongDbError = true)
        }
    }

    private fun formatLatLong(value: Float): String {
        return "%.4f".format(
            locale = Locale.getDefault(),
            value,
        )
    }
}
