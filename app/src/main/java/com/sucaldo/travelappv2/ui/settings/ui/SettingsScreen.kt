package com.sucaldo.travelappv2.ui.settings

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
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
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.ui.common.TopBar
import com.sucaldo.travelappv2.ui.common.TravelAppToast
import com.sucaldo.travelappv2.ui.settings.ui.SettingsHomeLocation
import com.sucaldo.travelappv2.ui.settings.ui.SettingsImportExport

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
                onSaveHomeLocation = { settingsViewModel.saveHomeLocation() },
                onOpenFile = {settingsViewModel.readCsv(it)},
                onGetData = {settingsViewModel.getGeoData()},
                location = settingsUiState.readLoc,
            )
        }
    }
}

@Composable
private fun SettingsScreenContent(
    homeCountry: String,
    homeCity: String,
    homeLocationErrorType: FieldErrorType,
    onSaveHomeLocation: () -> Unit,
    onOpenFile: (Uri) -> Unit,
    onGetData: () -> Unit,
    location: CityLocation?,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SettingsHomeLocation(
            homeCountry = homeCountry,
            homeCity = homeCity,
            homeLocationErrorType = homeLocationErrorType,
            onSaveHomeLocation = onSaveHomeLocation,
        )
        SettingsImportExport(onOpenFile = onOpenFile, onGetData = onGetData, location = location)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        homeCountry = "Asd",
        homeCity = "dfsd",
        homeLocationErrorType = FieldErrorType.NONE,
        onSaveHomeLocation = {},
        onOpenFile = {},
        onGetData = {},
        location = null,
        )
}
