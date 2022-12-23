package com.sucaldo.travelappv2.ui.trip

import com.sucaldo.travelappv2.data.TripType

data class TripUiState(
    val tripUiType: TripUiType = TripUiType.NEW,
    val tripDialogState: TripDialogState = TripDialogState.NONE,
    val tripType: TripType = TripType.RETURN,
    val fromCountry: String = "",
    val fromCountryErrorType: TripErrorType = TripErrorType.NONE,
    val fromCity: String = "",
    val fromCityErrorType: TripErrorType = TripErrorType.NONE,
    val fromLatitudeText: String = "",
    val fromLongitudeText: String = "",
    val fromLatLongErrorType:TripErrorType = TripErrorType.NONE,
    val toCountry: String = "",
    val toCountryErrorType: TripErrorType = TripErrorType.NONE,
    val toCity: String = "",
    val toCityErrorType: TripErrorType = TripErrorType.NONE,
    val toLatitudeText: String = "",
    val toLongitudeText: String = "",
    val toLatLongDbErrorType:TripErrorType = TripErrorType.NONE,
    val startDate: String = "",
    val startDateErrorType: TripErrorType = TripErrorType.NONE,
    val endDate: String = "",
    val endDateErrorType: TripErrorType = TripErrorType.NONE,
    val description: String = "",
    val descriptionErrorType: TripErrorType = TripErrorType.NONE,
    val countries: List<String> = listOf(),
)

enum class TripErrorType {
    NONE, LAT_LONG_DB, EMPTY, INVALID_CHARS, INVALID_DATE_FORMAT, INVALID_END_DATE, INVALID_START_DATE,
}

enum class TripUiType {
    NEW, EDIT,
}

enum class TripDialogState {
    NONE, EDIT_SUCCESS,
}