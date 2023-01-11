package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChartView
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.statistics.PlacesCloudType

@Composable
fun PlacesCloud(
    cloudType: PlacesCloudType,
    onInitChart: (AnyChartView) -> Unit,
    onUpdateChart: (PlacesCloudType) -> Unit,
) {
    val menuItems = listOf(
        ChartMenuItem(
            text = stringResource(id = R.string.trips_statistics_cloud_countries),
            selected = cloudType == PlacesCloudType.COUNTRIES,
            onClick = { onUpdateChart(PlacesCloudType.COUNTRIES) }
        ),
        ChartMenuItem(
            text = stringResource(id = R.string.trips_statistics_cloud_places),
            selected = cloudType == PlacesCloudType.PLACES,
            onClick = { onUpdateChart(PlacesCloudType.PLACES) }
        )
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