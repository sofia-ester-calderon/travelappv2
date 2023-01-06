package com.sucaldo.travelappv2.features.citycoordinates.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.TopBar

@Composable
fun CityCoordinatesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.title_city_coordinates)
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Text(text = "City Coordinates")
        }
    }
}