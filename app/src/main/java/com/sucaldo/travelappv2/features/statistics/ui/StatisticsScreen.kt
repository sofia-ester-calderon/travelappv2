package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.statistics.StatisticsType
import com.sucaldo.travelappv2.features.statistics.StatisticsViewModel
import kotlin.math.abs

@Composable
fun StatisticsScreen(
    navController: NavController,
    statisticsViewModel: StatisticsViewModel = viewModel(),
) {
    val statisticsUiState by statisticsViewModel.uiState.collectAsState()
    var direction by remember { mutableStateOf(SwipeDirection.NONE) }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.title_statistics_top_places)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        if (abs(x) > abs(y)) {
                            when {
                                x > 0 -> direction = SwipeDirection.RIGHT
                                x < 0 -> direction = SwipeDirection.LEFT
                            }
                        }
                    },
                    onDragEnd = {
                        when (direction) {
                            SwipeDirection.RIGHT -> statisticsViewModel.goToPreviousStatistic()
                            SwipeDirection.LEFT -> statisticsViewModel.goToNextStatistic()
                            else -> return@detectDragGestures
                        }
                    }
                )
            }) {
            Column {
                when (statisticsUiState.statisticsType) {
                    StatisticsType.TOP_PLACES -> TopTenChart(
                        topPlacesType = statisticsUiState.topTenType,
                        onInitChart = { statisticsViewModel.setTopTenChart(it) },
                        onUpdateChart = { statisticsViewModel.updateTopTenChart(it) })
                    StatisticsType.PLACES_CLOUD -> PlacesCloud(
                        cloudType = statisticsUiState.placesCloudType,
                        onInitChart = { statisticsViewModel.setPlacesCloudChart(it) },
                        onUpdateChart = { statisticsViewModel.updatePlacesClouds(it) },
                    )
                    StatisticsType.DISTANCE_CONTINENT -> DistanceChart(
                        onInitChart = { statisticsViewModel.setDistanceAreaChart(it) }
                    )
                    StatisticsType.DISTANCE_BUBBLE -> BubbleChart(
                        onInitChart = { statisticsViewModel.setBubbleChart(it) }
                    )
                }

            }
        }
    }
}

enum class SwipeDirection {
    NONE, LEFT, RIGHT,
}