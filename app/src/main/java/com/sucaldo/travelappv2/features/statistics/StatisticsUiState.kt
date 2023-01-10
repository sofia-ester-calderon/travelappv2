package com.sucaldo.travelappv2.features.statistics

import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Cartesian

data class StatisticsUiState(
    val statisticsType: StatisticsType = StatisticsType.TOP_PLACES,
    val topTenData: List<DataEntry> = listOf(),
    val topTenType: TopPlacesType = TopPlacesType.OVERALL,
    val topTenBarChart: Cartesian? = null,
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