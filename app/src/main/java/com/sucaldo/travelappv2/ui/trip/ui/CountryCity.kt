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

@Composable
fun CountryCity(
    country: String,
    city: String,
    latitude: String,
    longitude: String,
    latLongDbError: Boolean,
    onChangeCountry: (String) -> Unit,
    onChangeCity: (String) -> Unit,
    onChangeLatitude: (String) -> Unit,
    onChangeLongitude: (String) -> Unit,
    onCalculateLatLong: () -> Unit,
) {
    Country(country = country, onChangeCountry = onChangeCountry)
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = city,
        onValueChange = { onChangeCity(it) },
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
                value = latitude,
                isError = latLongDbError,
                onValueChange = { onChangeLatitude(it) },
                onCalculateLatLong = onCalculateLatLong,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            LongitudeLatitude(
                label = stringResource(id = R.string.trip_label_longitude),
                value = longitude,
                isError = latLongDbError,
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
) {
    // TODO: actual countries
    val options = listOf("Australia", "Germany", "El Salvador", "Namibia", "Switzerland")
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
            city = "Sydney",
            country = "Australia",
            latitude = "123.2",
            longitude = "123.45",
            latLongDbError = false,
            onChangeCountry = {},
            onCalculateLatLong = {},
            onChangeCity = {},
            onChangeLatitude = {},
            onChangeLongitude = {},
        )
    }
}