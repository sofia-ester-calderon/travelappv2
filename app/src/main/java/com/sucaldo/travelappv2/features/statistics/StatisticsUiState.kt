package com.sucaldo.travelappv2.features.statistics

import com.anychart.charts.Cartesian
import com.anychart.charts.TagCloud
import com.sucaldo.travelappv2.data.Trip

data class StatisticsUiState(
    val statisticsType: StatisticsType = StatisticsType.OVERVIEW,
    val topTenType: TopPlacesType = TopPlacesType.OVERALL,
    val topTenBarChart: Cartesian? = null,

    val placesCloudType: PlacesCloudType = PlacesCloudType.COUNTRIES,
    val placesCloudChart: TagCloud? = null,

    val worldTimesTravelled: Float = 0f,
    val numberOfVisitedCountries: Int = 0,
    val numberOfVisitedPlaces: Int = 0,
    val numberOfTrips: Int = 0,
    val randomTrip: Trip? = null,
)

enum class StatisticsType {
    OVERVIEW,
    TOP_PLACES,
    PLACES_CLOUD,
    DISTANCE_CONTINENT,
    DISTANCE_BUBBLE,
}

enum class TopPlacesType {
    OVERALL,
    LAST_TWO,
    LAST_FIVE,
    LAST_TEN,
}

enum class PlacesCloudType {
    COUNTRIES,
    PLACES,
}