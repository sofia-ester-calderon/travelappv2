package com.sucaldo.travelappv2.features.trips

import com.sucaldo.travelappv2.data.Trip

data class TripsUiState(
    val trips: List<Trip> = listOf(),
    val tripYears: List<TripYear> = listOf(),
    val tripDetail: Trip? = null,
    val showDeleteDialog: Boolean = false,
)

data class TripYear(
    val year: Int,
    var trips: List<Trip>? = null,
)