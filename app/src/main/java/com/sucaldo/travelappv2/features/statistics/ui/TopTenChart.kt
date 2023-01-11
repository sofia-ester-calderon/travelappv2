package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChartView
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.statistics.TopPlacesType

@Composable
fun TopTenChart(
    topPlacesType: TopPlacesType,
    onInitChart: (AnyChartView) -> Unit,
    onUpdateChart: (TopPlacesType) -> Unit,
) {
    val menuItems = listOf(
        ChartMenuItem(
            text = stringResource(id = R.string.trips_statistics_top_ten_overall),
            selected = topPlacesType == TopPlacesType.OVERALL,
            onClick = { onUpdateChart(TopPlacesType.OVERALL) },
        ),
        ChartMenuItem(
            text = stringResource(id = R.string.trips_statistics_top_ten_last_two),
            selected = topPlacesType == TopPlacesType.LAST_TWO,
            onClick = { onUpdateChart(TopPlacesType.LAST_TWO) },
        ),

        ChartMenuItem(
            text = stringResource(id = R.string.trips_statistics_top_ten_last_five),
            selected = topPlacesType == TopPlacesType.LAST_FIVE,
            onClick = { onUpdateChart(TopPlacesType.LAST_FIVE) },
        ),

        ChartMenuItem(
            text = stringResource(id = R.string.trips_statistics_top_ten_last_ten),
            selected = topPlacesType == TopPlacesType.LAST_TEN,
            onClick = { onUpdateChart(TopPlacesType.LAST_TEN) },
        ),
    )
    ChartMenu(menuItems)
    AndroidView(
        factory = {
            val chart = AnyChartView(it)
            onInitChart(chart)
            chart
        }, modifier = Modifier
            .fillMaxSize()
    )
}

