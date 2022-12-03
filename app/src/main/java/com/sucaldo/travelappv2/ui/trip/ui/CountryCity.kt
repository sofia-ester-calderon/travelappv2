package com.sucaldo.travelappv2.ui.trip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.trip.TripUiState
import com.sucaldo.travelappv2.ui.trip.TripViewModel

@Composable
fun CountryCity(
    tripUiState: TripUiState,
    tripViewModel: TripViewModel,
) {
    Country(country = tripUiState.fromCountry, tripViewModel = tripViewModel)
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = tripUiState.fromCity,
        onValueChange = { tripViewModel.changeFromCity(it) },
        label = { Text(stringResource(id = R.string.trip_label_city)) },
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        Column(
            modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp / 2f),
        ) {
            LongitudeLatitude(
                label = "Latitude",
                isError = tripUiState.latitudeLongitudeError,
                tripViewModel = tripViewModel,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            LongitudeLatitude(
                label = "Longitude",
                isError = tripUiState.latitudeLongitudeError,
                tripViewModel = tripViewModel,
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Country(country: String, tripViewModel: TripViewModel) {
    // TODO: actual countries
    val options = listOf("Australia", "Germany", "El Salvador", "Namibia", "Switzerland")
    var exp by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = country,
            onValueChange = { tripViewModel.changeFromCountry(it) },
            label = { Text(stringResource(id = R.string.trip_label_country)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        // filter options based on text field value (i.e. crude autocomplete)
        val filterOpts = options.filter { it.contains(country, ignoreCase = true) }
        if (filterOpts.isNotEmpty()) {
            ExposedDropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
                filterOpts.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            exp = false
                            tripViewModel.changeFromCountry(option)
                        }
                    ) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}

@Composable
private fun LongitudeLatitude(
    label: String,
    isError: Boolean,
    tripViewModel: TripViewModel,
) {
    TextField(
        value = "",
        onValueChange = { tripViewModel.onCalculateLatLong() },
        label = { Text(label) },
        isError = isError,
        trailingIcon = {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.Black
                )
            }
        }
    )
}

@Preview
@Composable
fun CountryCityPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        CountryCity(
            tripUiState = TripUiState(fromCity = "Sydney", fromCountry = "Australia"),
            tripViewModel = viewModel()
        )
    }
}