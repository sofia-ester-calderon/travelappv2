package com.sucaldo.travelappv2.features.statistics

import com.anychart.charts.Cartesian
import com.anychart.charts.TagCloud

data class StatisticsUiState(
    val statisticsType: StatisticsType = StatisticsType.TOP_PLACES,
    val topTenType: TopPlacesType = TopPlacesType.OVERALL,
    val topTenBarChart: Cartesian? = null,

    val placesCloudType: PlacesCloudType = PlacesCloudType.COUNTRIES,
    val placesCloudChart: TagCloud? = null,
)

enum class StatisticsType {
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