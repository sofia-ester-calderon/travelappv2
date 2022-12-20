package com.sucaldo.travelappv2.ui.trip

import com.sucaldo.travelappv2.data.TripType

data class TripUiState(
    val tripType: TripType = TripType.RETURN,
    val fromCountry: String = "",
    val fromCity: String = "",
    val fromLatitudeText: String = "",
    val fromLongitudeText: String = "",
    val fromLatLongErrorType:TripErrorType = TripErrorType.NONE,
    val toCountry: String = "",
    val toCity: String = "",
    val toLatitudeText: String = "",
    val toLongitudeText: String = "",
    val toLatLongDbErrorType:TripErrorType = TripErrorType.NONE,
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val countries: List<String> = listOf(),
)

enum class TripErrorType {
    NONE, LAT_LONG_DB, EMPTY, INVALID_CHARS,
}