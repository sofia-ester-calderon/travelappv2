package com.sucaldo.travelappv2.ui.trip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.trip.TripErrorType

@Composable
fun CountryCity(
    country: String,
    city: String,
    latitude: String,
    longitude: String,
    latLongError: TripErrorType,
    countryError: TripErrorType,
    cityError: TripErrorType,
    countries: List<String>,
    onChangeCountry: (String) -> Unit,
    onChangeCity: (String) -> Unit,
    onChangeLatitude: (String) -> Unit,
    onChangeLongitude: (String) -> Unit,
    onCalculateLatLong: () -> Unit,
) {
    Country(
        country = country,
        onChangeCountry = onChangeCountry,
        countries = countries,
        errorType = countryError,
    )
    Spacer(modifier = Modifier.height(8.dp))
    TravelAppTextField(
        modifier = Modifier.fillMaxWidth(),
        value = city,
        onValueChange = { onChangeCity(it) },
        label = (stringResource(id = R.string.trip_label_city)),
        errorText = getErrorText(cityError)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        Column(
            modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp / 2f),
        ) {
            LongitudeLatitude(
                label = stringResource(id = R.string.trip_label_latitude),
                value = latitude,
                errorType = latLongError,
                onValueChange = { onChangeLatitude(it) },
                onCalculateLatLong = onCalculateLatLong,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            LongitudeLatitude(
                label = stringResource(id = R.string.trip_label_longitude),
                value = longitude,
                errorType = latLongError,
                onValueChange = { onChangeLongitude(it) },
                onCalculateLatLong = onCalculateLatLong,
            )
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Country(
    country: String,
    onChangeCountry: (String) -> Unit,
    countries: List<String>,
    errorType: TripErrorType,
) {
    val options = countries
    var exp by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = country,
            onValueChange = { onChangeCountry(it) },
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
                            onChangeCountry(option)
                        }
                    ) {
                        Text(text = option)
                    }
                }
            }
        }
    }

    val errorText = getErrorText(errorType)
    if (errorText != null) {
        ErrorText(text = errorText)
    }
}

@Composable
private fun LongitudeLatitude(
    label: String,
    value: String,
    errorType: TripErrorType,
    onValueChange: (String) -> Unit,
    onCalculateLatLong: () -> Unit,
) {
    Column {
        TravelAppTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            icon = Icons.Default.Refresh,
            iconDescription = stringResource(id = R.string.trip_icon_refresh),
            onClickIcon = onCalculateLatLong,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            errorText = getErrorText(tripErrorType = errorType)
        )
    }

}

@Preview
@Composable
fun CountryCityPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        CountryCity(
            city = "Sydney",
            country = "Australia",
            latitude = "123.2",
            longitude = "123.45",
            latLongError = TripErrorType.NONE,
            countryError = TripErrorType.EMPTY,
            cityError = TripErrorType.NONE,
            countries = listOf(),
            onChangeCountry = {},
            onCalculateLatLong = {},
            onChangeCity = {},
            onChangeLatitude = {},
            onChangeLongitude = {},
        )
    }
}