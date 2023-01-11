package com.sucaldo.travelappv2.features.worldmap.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.TopBar
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
    ) {
        paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            WorldMapContent()
        }
    }
}

@Composable
private fun WorldMapContent() {
    Text(text = "WORLD MAP")
}