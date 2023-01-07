package com.sucaldo.travelappv2.features.citycoordinates.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.citycoordinates.CityCoordinatesViewModel
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.common.ui.CountryCity
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.settings.CityCoordinatesUiState

@Composable
fun CityCoordinatesScreen(
    navController: NavController,
    cityCoordinatesViewModel: CityCoordinatesViewModel = viewModel()
) {
    val cityCoordUiState by cityCoordinatesViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.title_city_coordinates)
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            CityCoordinatesContent(
                cityCoordUiState = cityCoordUiState,
                onChangeCountry = { cityCoordinatesViewModel.updateCountry(it) },
                onChangeCity = { cityCoordinatesViewModel.updateCity(it) },
                onSearchLocation = { cityCoordinatesViewModel.searchLocation() },
            )
        }
    }
}

@Composable
fun CityCoordinatesContent(
    cityCoordUiState: CityCoordinatesUiState,
    onChangeCountry: (String) -> Unit,
    onChangeCity: (String) -> Unit,
    onSearchLocation: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BigLabel(text = stringResource(id = R.string.city_coord_location))
        CountryCity(
            country = cityCoordUiState.country,
            city = cityCoordUiState.city,
            countryError = cityCoordUiState.countryErrorType,
            cityError = cityCoordUiState.cityErrorType,
            onChangeCountry = onChangeCountry,
            onChangeCity = onChangeCity,
        )
        Button(onClick = onSearchLocation) {
            Text(text = stringResource(id = R.string.city_coord_search))
        }
    }
}

@Preview
@Composable
fun CityCoordinatesPreview() {
    CityCoordinatesContent(CityCoordinatesUiState(city = "x", country = ""), {}, {}, {})
}