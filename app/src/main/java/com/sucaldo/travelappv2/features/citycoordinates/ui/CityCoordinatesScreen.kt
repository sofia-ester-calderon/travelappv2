package com.sucaldo.travelappv2.features.citycoordinates.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.features.citycoordinates.CityCoordinatesViewModel
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.common.ui.CountryCity
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.citycoordinates.CityCoordinatesUiState

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
        Spacer(modifier = Modifier.height(8.dp))
        if (cityCoordUiState.cityCoordinates.isNotEmpty()) {
            LocationList(locations = cityCoordUiState.cityCoordinates)
        }
    }
}

@Composable
fun LocationList(locations: List<CityLocation>) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        item {
            LocationRow(
                stringResource(id = R.string.common_country),
                stringResource(id = R.string.common_city),
                stringResource(id = R.string.common_latitude),
                stringResource(id = R.string.common_longitude),
                fontWeight = FontWeight.Bold,
            )
        }
        items(items = locations) { location ->
            LocationRow(
                location.country,
                location.city,
                location.latitude.toString(),
                location.longitude.toString()
            )


        }
    }
}

@Composable
private fun LocationRow(
    item1: String,
    item2: String,
    item3: String,
    item4: String,
    fontWeight: FontWeight? = null
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = item1, modifier = Modifier.weight(1f), fontWeight = fontWeight)
        Text(text = item2, modifier = Modifier.weight(1f), fontWeight = fontWeight)
        Text(text = item3, modifier = Modifier.weight(1f), fontWeight = fontWeight)
        Text(text = item4, modifier = Modifier.weight(1f), fontWeight = fontWeight)
    }
}

@Preview
@Composable
fun CityCoordinatesPreview() {
    CityCoordinatesContent(
        CityCoordinatesUiState(
        city = "x", country = "", cityCoordinates = listOf(
            CityLocation(
                country = "Australia",
                city = "Sydney",
                latitude = 12f,
                longitude = 2f
            ),
            CityLocation(
                country = "Australia",
                city = "Melbourne",
                latitude = 12f,
                longitude = 2f
            ),
        )
    ), {}, {}, {})
}