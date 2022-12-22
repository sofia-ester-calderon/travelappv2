package com.sucaldo.travelappv2.ui.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.sucaldo.travelappv2.data.*
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TripViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper
    private val appPreferences: AppPreferences
    private val tripId: String? = savedStateHandle["tripId"]

    init {
        myDb = DatabaseHelper(application.applicationContext)
        appPreferences = AppPreferences(application.applicationContext, myDb)
        if (tripId != null) {
            loadTrip(tripId)
        } else {
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
        }
    }

    private fun loadTrip(tripId: String) {
        val trip = myDb.getTripById(tripId.toInt())
        val fromLocation = myDb.getLocationOfCity(trip.fromCountry, trip.fromCity)
        val toLocation = myDb.getLocationOfCity(trip.toCountry, trip.toCity)
        _uiState.value = _uiState.value.copy(
            tripType = trip.type,
            fromCountry = trip.fromCountry,
            fromCity = trip.fromCity,
            fromLatitudeText = fromLocation.latitude.toString(),
            fromLongitudeText = fromLocation.longitude.toString(),
            toCountry = trip.toCountry,
            toCity = trip.toCity,
            toLatitudeText = toLocation.latitude.toString(),
            toLongitudeText = toLocation.longitude.toString(),
            startDate = trip.getPickerFormattedStartDate(),
            endDate = trip.getPickerFormattedEndDate(),
            description = trip.description,
        )
    }

    fun updateTripType(tripType: TripType) {
        _uiState.value = _uiState.value.copy(tripType = tripType)
    }

    fun updateFromCountry(country: String) {
        if (country.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromCountryErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromCountry = country)
    }

    fun updateFromCity(city: String) {
        if (city.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromCityErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromCity = city)
    }

    fun updateFromLatitude(latitude: String) {
        if (latitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromLatLongErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromLatitudeText = latitude)
    }

    fun updateFromLongitude(longitude: String) {
        if (longitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromLatLongErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromLongitudeText = longitude)
    }

    fun onFromCalculateLatLong() {
        _uiState.value = _uiState.value.copy(
            fromLatLongErrorType = TripErrorType.NONE,
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
            _uiState.value = uiState.value.copy(fromLatLongErrorType = TripErrorType.LAT_LONG_DB)
        }
    }

    fun updateToCountry(country: String) {
        if (country.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toCountryErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toCountry = country)
    }

    fun updateToCity(city: String) {
        if (city.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toCityErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toCity = city)
    }

    fun updateToLatitude(latitude: String) {
        if (latitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toLatLongDbErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toLatitudeText = latitude)
    }

    fun updateToLongitude(longitude: String) {
        if (longitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toLatLongDbErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy( toLongitudeText = longitude)
    }

    fun onToCalculateLatLong() {
        _uiState.value = _uiState.value.copy(
            toLatLongDbErrorType = TripErrorType.NONE,
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
            _uiState.value = uiState.value.copy(toLatLongDbErrorType = TripErrorType.LAT_LONG_DB)
        }
    }

    fun updateStartDate(startDate: String) {
        if (startDate.isNotBlank()) {
            _uiState.value = _uiState.value.copy(startDateErrorType = TripErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(startDate = startDate)
    }

    fun updateEndDate(endDate: String) {
        if (endDate.isNotBlank()) {
            _uiState.value = _uiState.value.copy(endDateErrorType = TripErrorType.NONE)
        }
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

    fun saveTrip() {
        if (!isTripValid()) return

    }

    private fun isTripValid(): Boolean {
        val areFieldsEmpty = !areFieldsFilled()
        val areFieldsInvalid = !areFieldsValid()
        val areDatesInvalid = !areDatesValid()
        return areFieldsEmpty || areFieldsInvalid || areDatesInvalid
    }

    private fun areFieldsFilled(): Boolean {
        var areFieldsEmpty = true
        if (_uiState.value.fromCountry.isBlank()) {
            _uiState.value = _uiState.value.copy(fromCountryErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.fromCity.isBlank()) {
            _uiState.value = _uiState.value.copy(fromCityErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.fromLatitudeText.isBlank() || _uiState.value.fromLongitudeText.isBlank()) {
            _uiState.value = _uiState.value.copy(fromLatLongErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.toCountry.isBlank()) {
            _uiState.value = _uiState.value.copy(toCountryErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.toCity.isBlank()) {
            _uiState.value = _uiState.value.copy(toCityErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.toLatitudeText.isBlank() || _uiState.value.toLongitudeText.isBlank()) {
            _uiState.value = _uiState.value.copy(toLatLongDbErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.startDate.isBlank()) {
            _uiState.value = _uiState.value.copy(startDateErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.tripType != TripType.ONE_WAY && _uiState.value.endDate.isBlank()) {
            _uiState.value = _uiState.value.copy(endDateErrorType = TripErrorType.EMPTY)
            areFieldsEmpty = false
        }
        return areFieldsEmpty
    }

    private fun areFieldsValid(): Boolean {
        var hasOnlyValidChars = true
        if (hasInvalidCharacters(_uiState.value.fromCity)) {
            _uiState.value = _uiState.value.copy(fromCityErrorType = TripErrorType.INVALID_CHARS)
            hasOnlyValidChars = false
        }
        if (hasInvalidCharacters(_uiState.value.toCity)) {
            _uiState.value = _uiState.value.copy(toCityErrorType = TripErrorType.INVALID_CHARS)
            hasOnlyValidChars = false
        }
        if (hasInvalidCharacters(_uiState.value.description)) {
            _uiState.value = _uiState.value.copy(descriptionErrorType = TripErrorType.INVALID_CHARS)
            hasOnlyValidChars = false
        }
        return hasOnlyValidChars
    }

    private fun hasInvalidCharacters(text: String): Boolean {
        return text.contains(",") || text.contains("'")
    }

    private fun areDatesValid(): Boolean {
        val formatter = SimpleDateFormat(DateFormat.PRETTY, Locale.getDefault())
        val startDate: Date?
        val endDate: Date?
        try {
            startDate = formatter.parse(_uiState.value.startDate)
        } catch (e: ParseException) {
            _uiState.value =
                _uiState.value.copy(startDateErrorType = TripErrorType.INVALID_DATE_FORMAT)
            e.printStackTrace()
            return false
        }
        if (_uiState.value.tripType != TripType.ONE_WAY) {
            try {
                endDate = formatter.parse(_uiState.value.endDate)
            } catch (e: ParseException) {
                _uiState.value =
                    _uiState.value.copy(endDateErrorType = TripErrorType.INVALID_DATE_FORMAT)
                e.printStackTrace()
                return false
            }
            if (endDate.after(startDate)) {
                _uiState.value =
                    _uiState.value.copy(endDateErrorType = TripErrorType.INVALID_END_DATE)
                return false
            }
        }
        if (_uiState.value.tripType == TripType.MULTI) {
            val previousTrip: Trip = myDb.getTripById(myDb.lastTripId)
            val endDatePreviousStop: Date? = previousTrip.endDate
            if (endDatePreviousStop == null || endDatePreviousStop.after(startDate)) {
                _uiState.value =
                    _uiState.value.copy(startDateErrorType = TripErrorType.INVALID_START_DATE)
                return false
            }
        }
        return true
    }

}
