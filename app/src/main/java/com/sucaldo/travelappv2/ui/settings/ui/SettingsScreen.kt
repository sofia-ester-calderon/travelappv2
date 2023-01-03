package com.sucaldo.travelappv2.ui.settings

import androidx.compose.foundation.layout.*
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
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.ui.common.BigLabel
import com.sucaldo.travelappv2.ui.common.CountryCity
import com.sucaldo.travelappv2.ui.common.TopBar
import com.sucaldo.travelappv2.ui.common.TravelAppToast

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val settingsUiState by settingsViewModel.uiState.collectAsState()
    if (settingsUiState.homeLocationSaveSuccessful) {
        TravelAppToast(message = stringResource(id = R.string.settings_home_loc_success))
    }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.title_settings),
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            SettingsScreenContent(
                homeCountry = settingsUiState.homeCountry,
                homeCity = settingsUiState.homeCity,
                homeLocationErrorType = settingsUiState.homeLocationErrorType,
                countries = settingsUiState.countries,
                onSaveHomeLocation = { settingsViewModel.saveHomeLocation() },
            )
        }
    }
}

@Composable
private fun SettingsScreenContent(
    homeCountry: String,
    homeCity: String,
    homeLocationErrorType: FieldErrorType,
    countries: List<String>,
    onSaveHomeLocation: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BigLabel(text = stringResource(id = R.string.settings_home_loc_title))
        CountryCity(
            country = homeCountry,
            city = homeCity,
            cityError = homeLocationErrorType,
            countries = countries,
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
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        countries = listOf(),
        homeCountry = "Asd",
        homeCity = "dfsd",
        homeLocationErrorType = FieldErrorType.NONE,
        onSaveHomeLocation = {})
}
