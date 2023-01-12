package com.sucaldo.travelappv2.features.citycoordinates.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.features.citycoordinates.CityCoordinatesViewModel
import com.sucaldo.travelappv2.features.citycoordinates.CityCoordinatesUiState
import com.sucaldo.travelappv2.features.common.ui.*

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
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CityCoordinatesContent(
                cityCoordUiState = cityCoordUiState,
                onChangeCountry = { cityCoordinatesViewModel.updateCountry(it) },
                onChangeCity = { cityCoordinatesViewModel.updateCity(it) },
                onSearchLocation = { cityCoordinatesViewModel.searchLocation() },
                onCloseDetails = { cityCoordinatesViewModel.closeDetails() },
                onOpenDetails = { cityCoordinatesViewModel.openDetails(it) },
                onUpdateLatitude = { cityCoordinatesViewModel.updateLatitude(it) },
                onUpdateLongitude = { cityCoordinatesViewModel.updateLongitude(it) },
                onSaveCoordinates = { cityCoordinatesViewModel.updateCityLocation() },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CityCoordinatesContent(
    cityCoordUiState: CityCoordinatesUiState,
    onChangeCountry: (String) -> Unit,
    onChangeCity: (String) -> Unit,
    onSearchLocation: () -> Unit,
    onCloseDetails: () -> Unit,
    onOpenDetails: (CityLocation) -> Unit,
    onUpdateLatitude: (String) -> Unit,
    onUpdateLongitude: (String) -> Unit,
    onSaveCoordinates: () -> Unit,
) {
    val bottomDrawerState = remember {
        BottomDrawerState(
            BottomDrawerValue.Closed,
            confirmStateChange = { bottomDrawerValue ->
                if (bottomDrawerValue == BottomDrawerValue.Closed) {
                    onCloseDetails()
                }
                true
            }
        )
    }
    BottomDrawerLaunchedEffect(
        state = bottomDrawerState,
        isBottomDrawerOpen = cityCoordUiState.selectedCityCoordinate != null
    )

    BottomDrawer(
        drawerState = bottomDrawerState,
        gesturesEnabled = bottomDrawerState.isOpen,
        drawerContent = {
            if (cityCoordUiState.selectedCityCoordinate != null) {
                CityCoordinateEdit(
                    cityLocation = cityCoordUiState.selectedCityCoordinate,
                    onUpdateLatitude = onUpdateLatitude,
                    onUpdateLongitude = onUpdateLongitude,
                    onSave = onSaveCoordinates,
                )
            } else {
                Text(text = "NOTHING SELECTED")
            }
        }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            CityLocationSearchSection(
                country = cityCoordUiState.country,
                city = cityCoordUiState.city,
                countryErrorType = cityCoordUiState.countryErrorType,
                cityErrorType = cityCoordUiState.cityErrorType,
                onChangeCountry = onChangeCountry,
                onChangeCity = onChangeCity,
                onSearchLocation = onSearchLocation,
            )
            if (cityCoordUiState.cityCoordinates.isNotEmpty()) {
                LocationList(
                    locations = cityCoordUiState.cityCoordinates,
                    onOpenCityLocation = onOpenDetails
                )
            }
        }
    }
}

@Composable
private fun CityLocationSearchSection(
    country: String,
    city: String,
    countryErrorType: FieldErrorType,
    cityErrorType: FieldErrorType,
    onChangeCountry: (String) -> Unit,
    onChangeCity: (String) -> Unit,
    onSearchLocation: () -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.city_coord_location))
    CountryCity(
        country = country,
        city = city,
        countryError = countryErrorType,
        cityError = cityErrorType,
        onChangeCountry = onChangeCountry,
        onChangeCity = onChangeCity,
    )
    Button(onClick = onSearchLocation) {
        Text(text = stringResource(id = R.string.city_coord_search))
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomDrawerLaunchedEffect(
    state: BottomDrawerState,
    isBottomDrawerOpen: Boolean,
) {
    LaunchedEffect(isBottomDrawerOpen) {
        if (isBottomDrawerOpen) {
            state.open()
        } else {
            state.close()
        }
    }
}
