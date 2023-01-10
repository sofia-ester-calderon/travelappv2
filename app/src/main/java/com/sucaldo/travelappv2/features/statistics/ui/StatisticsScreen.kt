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
import com.sucaldo.travelappv2.features.statistics.StatisticsViewModel

@Composable
fun StatisticsScreen(
    navController: NavController,
    statisticsViewModel: StatisticsViewModel = viewModel(),
) {
    val statisticsUiState by statisticsViewModel.uiState.collectAsState()
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

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
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    when {
                        dragAmount.x > 0 -> {
                            statisticsViewModel.goToPreviousStatistic()
                        }
                        dragAmount.x < 0 -> {
                            statisticsViewModel.goToNextStatistic()
                        }
                    }
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }) {
            Column {
                TopTenChart(
                    onInitChart = { statisticsViewModel.setTopTenChart(it) },
                    onUpdateChart = { statisticsViewModel.updateTopTenChart(it) })
            }
        }
    }
}