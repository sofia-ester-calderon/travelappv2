package com.sucaldo.travelappv2.features.citycoordinates.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.common.ui.TravelAppTextField

@Composable
fun CityCoordinateEdit(
    cityLocation: CityLocation,
    onUpdateLatitude: (String) -> Unit,
    onUpdateLongitude: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp)) {
        BigLabel(text = "Edit Coordinates for ${cityLocation.city}, ${cityLocation.country}")
        Row {
            Column(
                modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp / 2f),
            ) {
                TravelAppTextField(
                    value = cityLocation.latitude.toString(),
                    onValueChange = onUpdateLatitude,
                    label = stringResource(id = R.string.common_latitude),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                TravelAppTextField(
                    value = cityLocation.longitude.toString(),
                    onValueChange = onUpdateLongitude,
                    label = stringResource(id = R.string.common_longitude),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSave) {
            Text(text = stringResource(id = R.string.common_save))
        }
    }
}