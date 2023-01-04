package com.sucaldo.travelappv2.ui.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.ui.common.BigLabel
import com.sucaldo.travelappv2.ui.common.CountryCity

@Composable
fun SettingsHomeLocation(
    homeCountry: String,
    homeCity: String,
    homeLocationErrorType: FieldErrorType,
    onSaveHomeLocation: () -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.settings_home_loc_title))
    CountryCity(
        country = homeCountry,
        city = homeCity,
        cityError = homeLocationErrorType,
        onChangeCountry = {},
        onChangeCity = {},
        countryError = homeLocationErrorType,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { onSaveHomeLocation() }) {
            Text(text = "Save")
        }
    }
}