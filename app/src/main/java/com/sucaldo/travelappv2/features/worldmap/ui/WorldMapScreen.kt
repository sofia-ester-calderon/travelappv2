package com.sucaldo.travelappv2.features.worldmap.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.worldmap.LocationCircle
import com.sucaldo.travelappv2.features.worldmap.WorldMapSize
import com.sucaldo.travelappv2.features.worldmap.WorldMapViewModel

@Composable
fun WorldMapScreen(
    navController: NavController,
    worldMapViewModel: WorldMapViewModel = viewModel(),
) {
    val worldMapUiState by worldMapViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.title_world_map)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            WorldMapContent(
                circleLocations = worldMapUiState.locationCircles,
                onPositioned = { worldMapViewModel.drawCircles(it) },
            )
        }
    }
}

@Composable
private fun WorldMapContent(
    circleLocations: List<LocationCircle>,
    onPositioned: (WorldMapSize) -> Unit,
) {
    Image(
        painterResource(id = R.drawable.world_map),
        contentDescription = "World Map",
        modifier = Modifier.onGloballyPositioned { coordinates ->
            onPositioned(
                WorldMapSize(
                    height = coordinates.size.height.toFloat(),
                    width = coordinates.size.width.toFloat(),
                )
            )
        })
    Canvas(modifier = Modifier
        .fillMaxSize(), onDraw = {
        circleLocations.forEach {
            drawCircle(
                Color.Red,
                radius = 5f,
                center = Offset(center.x + it.offsetX, center.y - it.offsetY)
            )
        }
    })
}
