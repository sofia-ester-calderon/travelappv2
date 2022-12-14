package com.sucaldo.travelappv2.ui.trip

import com.sucaldo.travelappv2.data.TripType

data class TripUiState(
    val tripType: TripType = TripType.RETURN,
    val fromCountry: String = "",
    val fromCity: String = "",
    val fromLatitudeText: String = "",
    val fromLongitudeText: String = "",
    val isFromLatLongDbError:Boolean = false,
    val toCountry: String = "",
    val toCity: String = "",
    val toLatitudeText: String = "",
    val toLongitudeText: String = "",
    val isToLatLongDbError:Boolean = false,
    val startDate: String = "",
    val endDate: String = "",
)