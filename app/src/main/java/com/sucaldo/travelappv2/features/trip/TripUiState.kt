package com.sucaldo.travelappv2.features.trip

import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.data.TripType

data class TripUiState(
    val tripUiType: TripUiType = TripUiType.NEW,
    val tripDialogState: TripDialogState = TripDialogState.NONE,
    val tripType: TripType = TripType.RETURN,
    val fromCountry: String = "",
    val fromCountryErrorType: FieldErrorType = FieldErrorType.NONE,
    val fromCity: String = "",
    val fromCityErrorType: FieldErrorType = FieldErrorType.NONE,
    val fromLatitudeText: String = "",
    val fromLongitudeText: String = "",
    val fromLatLongErrorType:FieldErrorType = FieldErrorType.NONE,
    val toCountry: String = "",
    val toCountryErrorType: FieldErrorType = FieldErrorType.NONE,
    val toCity: String = "",
    val toCityErrorType: FieldErrorType = FieldErrorType.NONE,
    val toLatitudeText: String = "",
    val toLongitudeText: String = "",
    val toLatLongDbErrorType:FieldErrorType = FieldErrorType.NONE,
    val startDate: String = "",
    val startDateErrorType: FieldErrorType = FieldErrorType.NONE,
    val endDate: String = "",
    val endDateErrorType: FieldErrorType = FieldErrorType.NONE,
    val description: String = "",
    val descriptionErrorType: FieldErrorType = FieldErrorType.NONE,
)

enum class TripUiType {
    NEW, EDIT, NEW_STOP,
}

enum class TripDialogState {
    NONE, EDIT_SUCCESS, SAVE_ERROR, SIMPLE_TRIP, MULTI_TRIP,
}