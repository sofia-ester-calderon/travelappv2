package com.sucaldo.travelappv2.ui.trip

import com.sucaldo.travelappv2.data.TripType

data class TripUiState(
    val tripType: TripType = TripType.RETURN,
    val fromCountry: String = "",
    val fromCity: String = "",
    val fromLatitudeText: String = "",
    val fromLongitudeText: String = "",
    val isFromLatLongDbError:Boolean = false,
)