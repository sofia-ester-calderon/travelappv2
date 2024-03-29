package com.sucaldo.travelappv2.features.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.common.ui.CountryCity

@Composable
fun SettingsHomeLocation(
    homeCountry: String,
    homeCity: String,
    homeLocationErrorType: FieldErrorType,
    onSaveHomeLocation: () -> Unit,
    onChangeHomeCountry: (String) -> Unit,
    onChangeHomeCity: (String) -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.settings_home_loc_title))
    CountryCity(
        country = homeCountry,
        city = homeCity,
        cityError = homeLocationErrorType,
        onChangeCountry = { onChangeHomeCountry(it) },
        onChangeCity = { onChangeHomeCity(it) },
        countryError = homeLocationErrorType,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { onSaveHomeLocation() }) {
            Text(text = "Save")
        }
    }
}