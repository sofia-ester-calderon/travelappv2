package com.sucaldo.travelappv2.features.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sucaldo.travelappv2.data.*
import com.sucaldo.travelappv2.db.DatabaseHelper
import com.sucaldo.travelappv2.features.common.ui.Routes
import com.sucaldo.travelappv2.util.DateFormat
import com.sucaldo.travelappv2.util.DistanceCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
    private var groupId: Int? = null
    private var previousTrip: Trip? = null

    init {
        myDb = DatabaseHelper(application.applicationContext)
        appPreferences = AppPreferences(application.applicationContext, myDb)
        if (tripId != null) {
            _uiState.value = _uiState.value.copy(tripUiType = TripUiType.EDIT)
            loadTrip(tripId.toInt())
        } else {
            viewModelScope.launch {
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

    private fun loadTrip(tripId: Int) {
        val trip = myDb.getTripById(tripId)
        if (trip != null) {
            val fromLocation = myDb.getLocationOfCity(trip.fromCountry, trip.fromCity)
            val toLocation = myDb.getLocationOfCity(trip.toCountry, trip.toCity)
            groupId = trip.groupId
            _uiState.value = _uiState.value.copy(
                tripType = trip.type,
                fromCountry = trip.fromCountry,
                fromCity = trip.fromCity,
                fromLatitudeText = fromLocation?.latitude.toString(),
                fromLongitudeText = fromLocation?.longitude.toString(),
                toCountry = trip.toCountry,
                toCity = trip.toCity,
                toLatitudeText = toLocation?.latitude.toString(),
                toLongitudeText = toLocation?.longitude.toString(),
                startDate = trip.getPickerFormattedStartDate(),
                endDate = trip.getPickerFormattedEndDate(),
                description = trip.description,
            )
        }
    }

    fun updateTripType(tripType: TripType) {
        _uiState.value = _uiState.value.copy(tripType = tripType)
    }

    fun updateFromCountry(country: String) {
        if (country.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromCountryErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromCountry = country)
    }

    fun updateFromCity(city: String) {
        if (city.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromCityErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromCity = city)
    }

    fun updateFromLatitude(latitude: String) {
        if (latitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromLatLongErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromLatitudeText = latitude)
    }

    fun updateFromLongitude(longitude: String) {
        if (longitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(fromLatLongErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(fromLongitudeText = longitude)
    }

    fun onCalculateFromLatLong() {
        _uiState.value = _uiState.value.copy(
            fromLatLongErrorType = FieldErrorType.NONE,
            fromLongitudeText = "",
            fromLatitudeText = "",
        )
        val country = uiState.value.fromCountry
        val city = uiState.value.fromCity
        try {
            val cityLocation: CityLocation? = myDb.getLocationOfCity(country, city)
            _uiState.value = _uiState.value.copy(
                fromLatitudeText = formatLatLong(cityLocation!!.latitude)
            )
            _uiState.value = _uiState.value.copy(
                fromLongitudeText = formatLatLong(cityLocation.longitude)
            )
        } catch (e: java.lang.Exception) {
            _uiState.value = uiState.value.copy(fromLatLongErrorType = FieldErrorType.LAT_LONG_DB)
        }
    }

    fun updateToCountry(country: String) {
        if (country.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toCountryErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toCountry = country)
    }

    fun updateToCity(city: String) {
        if (city.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toCityErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toCity = city)
    }

    fun updateToLatitude(latitude: String) {
        if (latitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toLatLongDbErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toLatitudeText = latitude)
    }

    fun updateToLongitude(longitude: String) {
        if (longitude.isNotBlank()) {
            _uiState.value = _uiState.value.copy(toLatLongDbErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(toLongitudeText = longitude)
    }

    fun onCalculateToLatLong() {
        _uiState.value = _uiState.value.copy(
            toLatLongDbErrorType = FieldErrorType.NONE,
            toLongitudeText = "",
            toLatitudeText = "",
        )
        val country = uiState.value.toCountry
        val city = uiState.value.toCity
        try {
            val cityLocation: CityLocation? = myDb.getLocationOfCity(country, city)
            _uiState.value = _uiState.value.copy(
                toLatitudeText = formatLatLong(cityLocation!!.latitude)
            )
            _uiState.value = _uiState.value.copy(
                toLongitudeText = formatLatLong(cityLocation.longitude)
            )
        } catch (e: java.lang.Exception) {
            _uiState.value = uiState.value.copy(toLatLongDbErrorType = FieldErrorType.LAT_LONG_DB)
        }
    }

    fun updateStartDate(startDate: String) {
        if (startDate.isNotBlank()) {
            _uiState.value = _uiState.value.copy(startDateErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(startDate = startDate)
    }

    fun updateEndDate(endDate: String) {
        if (endDate.isNotBlank()) {
            _uiState.value = _uiState.value.copy(endDateErrorType = FieldErrorType.NONE)
        }
        _uiState.value = _uiState.value.copy(endDate = endDate)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onGoToMyTrips(navController: NavController) {
        navigateToMyTrips(navController)
    }

    fun onAddAnotherTrip() {
        removeAllUiErrors()
        groupId = null
        _uiState.value = _uiState.value.copy(
            tripUiType = TripUiType.NEW,
            tripDialogState = TripDialogState.NONE,
            tripType = TripType.RETURN,
            fromCity = "",
            fromCountry = "",
            fromLatitudeText = "",
            fromLongitudeText = "",
            toCountry = "",
            toCity = "",
            toLatitudeText = "",
            toLongitudeText = "",
            startDate = "",
            endDate = "",
            description = "",
        )
    }

    fun onAddNextStop() {
        removeAllUiErrors()
        previousTrip = myDb.getTripById(myDb.lastTripId)
        groupId = previousTrip!!.groupId
        _uiState.value = _uiState.value.copy(
            tripUiType = TripUiType.NEW_STOP,
            tripDialogState = TripDialogState.NONE,
            tripType = TripType.MULTI_STOP,
            fromCity = previousTrip!!.fromCity,
            fromCountry = previousTrip!!.fromCountry,
            fromLatitudeText = "",
            fromLongitudeText = "",
            toCountry = "",
            toCity = "",
            toLatitudeText = "",
            toLongitudeText = "",
            startDate = previousTrip!!.getPickerFormattedEndDate(),
            endDate = "",
            description = previousTrip!!.description,
        )
        onCalculateFromLatLong()
    }

    fun onCompleteStop(navController: NavController) {
        val trip = myDb.getTripById(myDb.lastTripId)
        trip!!.type = TripType.MULTI_STOP_LAST_STOP
        trip.toContinent = getContinentOfCountry(trip.fromCountry)
        myDb.updateTrip(trip)
        navigateToMyTrips(navController)
    }

    private fun removeAllUiErrors() {
        _uiState.value = _uiState.value.copy(
            fromCountryErrorType = FieldErrorType.NONE,
            fromCityErrorType = FieldErrorType.NONE,
            fromLatLongErrorType = FieldErrorType.NONE,
            toCountryErrorType = FieldErrorType.NONE,
            toCityErrorType = FieldErrorType.NONE,
            toLatLongDbErrorType = FieldErrorType.NONE,
            startDateErrorType = FieldErrorType.NONE,
            endDateErrorType = FieldErrorType.NONE,
            descriptionErrorType = FieldErrorType.NONE,
        )
    }

    private fun formatLatLong(value: Float): String {
        return "%.4f".format(
            locale = Locale.getDefault(),
            value,
        )
    }

    private fun getContinentOfCountry(country: String): String {
        return countryContinents.find { it.country == country }!!.continent
    }

    fun saveTrip(navController: NavController) {
        if (!isTripValid()) return
        val continent = getContinentOfCountry(_uiState.value.toCountry)
        val trip = Trip(
            id = tripId?.toInt(),
            groupId = groupId,
            fromCountry = _uiState.value.fromCountry,
            fromCity = _uiState.value.toCity,
            toCountry = _uiState.value.toCountry,
            toCity = _uiState.value.toCity,
            description = _uiState.value.description,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate,
            type = _uiState.value.tripType,
            toContinent = continent
        )
        val fromCityLocation = CityLocation(
            country = trip.fromCountry,
            city = trip.fromCity,
            latitude = _uiState.value.fromLatitudeText.toFloat(),
            longitude = _uiState.value.fromLongitudeText.toFloat(),
        )
        val toCityLocation = CityLocation(
            country = trip.toCountry,
            city = trip.toCity,
            latitude = _uiState.value.toLatitudeText.toFloat(),
            longitude = _uiState.value.toLongitudeText.toFloat(),
        )
        myDb.saveCityLocationIfNotInDb(fromCityLocation)
        myDb.saveCityLocationIfNotInDb(toCityLocation)
        val distance = DistanceCalculator.getDistanceFromLatLongInKms(
            lat1 = fromCityLocation.latitude,
            long1 = fromCityLocation.longitude,
            lat2 = toCityLocation.latitude,
            long2 = toCityLocation.longitude,
            isDoubleDistance = trip.type == TripType.RETURN,
        )
        trip.distance = distance
        when(_uiState.value.tripUiType) {
            TripUiType.NEW, TripUiType.NEW_STOP -> saveNewTrip(trip, navController)
            TripUiType.EDIT -> updateTrip(trip, navController)
        }
    }

    private fun saveNewTrip(trip: Trip, navController: NavController) {
        if (myDb.addTrip(trip)) {
            when(trip.type) {
                TripType.RETURN, TripType.ONE_WAY ->
                    _uiState.value = _uiState.value.copy(tripDialogState = TripDialogState.SIMPLE_TRIP)
                TripType.MULTI_STOP, TripType.MULTI_STOP_LAST_STOP ->
                    _uiState.value = _uiState.value.copy(tripDialogState = TripDialogState.MULTI_TRIP)
            }
        } else {
            _uiState.value = _uiState.value.copy(tripDialogState = TripDialogState.SAVE_ERROR)
            navigateToMyTrips(navController)
        }
    }

    private fun updateTrip(trip: Trip, navController: NavController) {
        myDb.updateTrip(trip)
        _uiState.value = _uiState.value.copy(tripDialogState = TripDialogState.EDIT_SUCCESS)
        navigateToMyTrips(navController)
    }

    private fun navigateToMyTrips(navController: NavController) {
        navController.navigate(Routes.HOME) //TODO: change to my trips
    }

    private fun isTripValid(): Boolean {
        return areFieldsFilled() && areFieldsValid() && areDatesValid()
    }

    private fun areFieldsFilled(): Boolean {
        var areFieldsEmpty = true
        if (_uiState.value.fromCountry.isBlank()) {
            _uiState.value = _uiState.value.copy(fromCountryErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.fromCity.isBlank()) {
            _uiState.value = _uiState.value.copy(fromCityErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.fromLatitudeText.isBlank() || _uiState.value.fromLongitudeText.isBlank()) {
            _uiState.value = _uiState.value.copy(fromLatLongErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.toCountry.isBlank()) {
            _uiState.value = _uiState.value.copy(toCountryErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.toCity.isBlank()) {
            _uiState.value = _uiState.value.copy(toCityErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.toLatitudeText.isBlank() || _uiState.value.toLongitudeText.isBlank()) {
            _uiState.value = _uiState.value.copy(toLatLongDbErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.startDate.isBlank()) {
            _uiState.value = _uiState.value.copy(startDateErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        if (_uiState.value.tripType != TripType.ONE_WAY && _uiState.value.endDate.isBlank()) {
            _uiState.value = _uiState.value.copy(endDateErrorType = FieldErrorType.EMPTY)
            areFieldsEmpty = false
        }
        return areFieldsEmpty
    }

    private fun areFieldsValid(): Boolean {
        var hasOnlyValidChars = true
        if (hasInvalidCharacters(_uiState.value.fromCity)) {
            _uiState.value = _uiState.value.copy(fromCityErrorType = FieldErrorType.INVALID_CHARS)
            hasOnlyValidChars = false
        }
        if (hasInvalidCharacters(_uiState.value.toCity)) {
            _uiState.value = _uiState.value.copy(toCityErrorType = FieldErrorType.INVALID_CHARS)
            hasOnlyValidChars = false
        }
        if (hasInvalidCharacters(_uiState.value.description)) {
            _uiState.value = _uiState.value.copy(descriptionErrorType = FieldErrorType.INVALID_CHARS)
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
                _uiState.value.copy(startDateErrorType = FieldErrorType.INVALID_DATE_FORMAT)
            e.printStackTrace()
            return false
        }
        if (_uiState.value.tripType != TripType.ONE_WAY) {
            try {
                endDate = formatter.parse(_uiState.value.endDate)
            } catch (e: ParseException) {
                _uiState.value =
                    _uiState.value.copy(endDateErrorType = FieldErrorType.INVALID_DATE_FORMAT)
                e.printStackTrace()
                return false
            }
            if (startDate.after(endDate)) {
                _uiState.value =
                    _uiState.value.copy(endDateErrorType = FieldErrorType.INVALID_END_DATE)
                return false
            }
        }
        if (_uiState.value.tripType == TripType.MULTI_STOP) {
            val endDatePreviousStop: Date? = previousTrip?.endDate
            if (endDatePreviousStop == null || endDatePreviousStop.after(startDate)) {
                _uiState.value =
                    _uiState.value.copy(startDateErrorType = FieldErrorType.INVALID_START_DATE)
                return false
            }
        }
        return true
    }
}
