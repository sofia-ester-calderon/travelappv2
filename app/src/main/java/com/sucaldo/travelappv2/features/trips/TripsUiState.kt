package com.sucaldo.travelappv2.features.trips

import com.sucaldo.travelappv2.data.Trip

data class TripsUiState(
    val trips: List<Trip> = listOf(),
    val tripYears: List<TripYear> = listOf(),
)

data class TripYear(
    val year: Int,
    var trips: List<Trip>? = null,
)