package com.sucaldo.travelappv2.features.settings.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.common.ui.TravelAppToast
import com.sucaldo.travelappv2.features.settings.ImportState
import com.sucaldo.travelappv2.features.settings.SettingsViewModel

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
                onChangeHomeCountry = { settingsViewModel.saveHomeCountry(it) },
                onChangeHomeCity = { settingsViewModel.saveHomeCity(it) },
                onSelectLocationFile = { settingsViewModel.importLocationDataFromCsv(it) },
                importGeoDataState = settingsUiState.importGeoDataState,
                importTripDataState = settingsUiState.importTripState,
                exportDataState = settingsUiState.exportDataState,
                onSelectTripFile = {settingsViewModel.importTripDataFromCsv(it)},
                onExportData = { settingsViewModel.exportAllData() }
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
    onChangeHomeCountry: (String) -> Unit,
    onChangeHomeCity: (String) -> Unit,
    onSelectLocationFile: (Uri) -> Unit,
    onSelectTripFile: (Uri) -> Unit,
    importGeoDataState: ImportState,
    importTripDataState: ImportState,
    exportDataState: ImportState,
    onExportData: () -> Unit,
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
            onChangeHomeCountry = onChangeHomeCountry,
            onChangeHomeCity = onChangeHomeCity,
        )
        SettingsImportExport(
            importGeoDataState = importGeoDataState,
            importTripDataState = importTripDataState,
            exportDataState = exportDataState,
            onSelectLocationFile = onSelectLocationFile,
            onSelectTripFile = onSelectTripFile,
            onExportData = onExportData
        )
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
        onSelectLocationFile = {},
        importGeoDataState = ImportState.Ready,
        importTripDataState = ImportState.ImportStarted.Error,
        onSelectTripFile = {},
        onChangeHomeCity = {},
        onChangeHomeCountry = {},
        onExportData = {},
        exportDataState = ImportState.Ready,
    )
}
