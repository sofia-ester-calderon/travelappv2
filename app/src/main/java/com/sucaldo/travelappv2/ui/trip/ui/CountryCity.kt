package com.sucaldo.travelappv2.ui.trip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.trip.TripUiState

@Composable
fun CountryCity(
    tripUiState: TripUiState,
    onChangeFromCountry: (String) -> Unit,
    onChangeFromCity: (String) -> Unit,
    onChangeFromLatitude: (String) -> Unit,
    onChangeFromLongitude: (String) -> Unit,
    onCalculateLatLong: () -> Unit,
) {
    Country(country = tripUiState.fromCountry, onChangeFromCountry = onChangeFromCountry)
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = tripUiState.fromCity,
        onValueChange = { onChangeFromCity(it) },
        singleLine = true,
        label = { Text(stringResource(id = R.string.trip_label_city)) },
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        Column(
            modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp / 2f),
        ) {
            LongitudeLatitude(
                label = stringResource(id = R.string.trip_label_latitude),
                value = tripUiState.fromLatitudeText,
                isError = tripUiState.isFromLatLongDbError,
                onValueChange = { onChangeFromLatitude(it) },
                onCalculateLatLong = onCalculateLatLong,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            LongitudeLatitude(
                label = stringResource(id = R.string.trip_label_longitude),
                value = tripUiState.fromLongitudeText,
                isError = tripUiState.isFromLatLongDbError,
                onValueChange = { onChangeFromLongitude(it) },
                onCalculateLatLong = onCalculateLatLong,
            )
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Country(
    country: String,
    onChangeFromCountry: (String) -> Unit,
) {
    // TODO: actual countries
    val options = listOf("Australia", "Germany", "El Salvador", "Namibia", "Switzerland")
    var exp by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = country,
            onValueChange = { onChangeFromCountry(it) },
            label = { Text(stringResource(id = R.string.trip_label_country)) },
            singleLine = true,
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
                            onChangeFromCountry(option)
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
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    onCalculateLatLong: () -> Unit,
) {
    Column {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            label = { Text(label) },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                IconButton(
                    onClick = { onCalculateLatLong() },
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.trip_icon_refresh),
                        tint = Color.Black
                    )
                }
            }
        )
        if (isError) {
            Text(text = "Not in DB. Add manually!", fontSize = 12.sp, color = Color.Red)
        }
    }

}

@Preview
@Composable
fun CountryCityPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        CountryCity(
            tripUiState = TripUiState(fromCity = "Sydney", fromCountry = "Australia"),
            onChangeFromCountry = {},
            onCalculateLatLong = {},
            onChangeFromCity = {},
            onChangeFromLatitude = {},
            onChangeFromLongitude = {},
        )
    }
}