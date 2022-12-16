package com.sucaldo.travelappv2.ui.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.AppPreferences
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.TripType
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.*

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper
    private val appPreferences: AppPreferences

    init {
        myDb = DatabaseHelper(application.applicationContext)
        appPreferences = AppPreferences(application.applicationContext, myDb)
        runBlocking {
            val storedHomeLocation = appPreferences.getSavedHomeLocation()
            if (storedHomeLocation != null) {
                _uiState.value = _uiState.value.copy(
                    fromCity = storedHomeLocation.city,
                    fromCountry = storedHomeLocation.country,
                    fromLatitudeText = storedHomeLocation.latitude.toString(),
                    fromLongitudeText = storedHomeLocation.longitude.toString(),
                )
            }
        }
        _uiState.value = _uiState.value.copy(countries = myDb.countries)
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
        _uiState.value =
            _uiState.value.copy(fromLatitudeText = latitude, isFromLatLongDbError = false)
    }

    fun updateFromLongitude(longitude: String) {
        _uiState.value =
            _uiState.value.copy(fromLongitudeText = longitude, isFromLatLongDbError = false)
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

    fun updateToCountry(country: String) {
        _uiState.value = _uiState.value.copy(toCountry = country)
    }

    fun updateToCity(city: String) {
        _uiState.value = _uiState.value.copy(toCity = city)
    }

    fun updateToLatitude(latitude: String) {
        _uiState.value = _uiState.value.copy(toLatitudeText = latitude, isToLatLongDbError = false)
    }

    fun updateToLongitude(longitude: String) {
        _uiState.value =
            _uiState.value.copy(toLongitudeText = longitude, isToLatLongDbError = false)
    }

    fun onToCalculateLatLong() {
        _uiState.value = _uiState.value.copy(
            isToLatLongDbError = false,
            toLongitudeText = "",
            toLatitudeText = "",
        )
        val country = uiState.value.toCountry
        val city = uiState.value.toCity
        try {
            val cityLocation: CityLocation = myDb.getLocationOfCity(country, city)
            _uiState.value = _uiState.value.copy(
                toLatitudeText = formatLatLong(cityLocation.latitude)
            )
            _uiState.value = _uiState.value.copy(
                toLongitudeText = formatLatLong(cityLocation.longitude)
            )
        } catch (e: java.lang.Exception) {
            _uiState.value = uiState.value.copy(isToLatLongDbError = true)
        }
    }

    fun updateStartDate(startDate: String) {
        _uiState.value = _uiState.value.copy(startDate = startDate)
    }

    fun updateEndDate(endDate: String) {
        _uiState.value = _uiState.value.copy(endDate = endDate)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    private fun formatLatLong(value: Float): String {
        return "%.4f".format(
            locale = Locale.getDefault(),
            value,
        )
    }
}
