package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChartView

@Composable
fun DistanceChart(
    onInitChart: (AnyChartView) -> Unit,
) {
    AndroidView(
        factory = {
            val chart = AnyChartView(it)
            onInitChart(chart)
            chart
        }, modifier = Modifier
            .fillMaxSize()
    )
}