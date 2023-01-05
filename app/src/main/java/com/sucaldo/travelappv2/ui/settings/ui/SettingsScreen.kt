package com.sucaldo.travelappv2.ui.settings.ui

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.sucaldo.travelappv2.ui.common.TopBar
import com.sucaldo.travelappv2.ui.common.TravelAppToast
import com.sucaldo.travelappv2.ui.settings.ImportState
import com.sucaldo.travelappv2.ui.settings.SettingsViewModel

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
                onSelectLocationFile = { settingsViewModel.importLocationDataFromCsv(it) },
                startThread = { settingsViewModel.startThread() },
                importGeoDataState = settingsUiState.importGeoDataState,
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
    onSelectLocationFile: (Uri) -> Unit,
    startThread: () -> Unit,
    importGeoDataState: ImportState,
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
        SettingsImportExport(
            importGeoDataState = importGeoDataState,
            onSelectLocationFile = onSelectLocationFile
        )

        Button(onClick = startThread) {
            Text(text = "Start thread")
        }
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
        startThread = {},
        importGeoDataState = ImportState.READY,
    )
}
