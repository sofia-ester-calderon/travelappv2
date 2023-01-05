package com.sucaldo.travelappv2.features.settings.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.settings.ImportState

@Composable
fun SettingsImportExport(
    importGeoDataState: ImportState,
    importTripDataState: ImportState,
    onSelectLocationFile: (Uri) -> Unit,
    onSelectTripFile: (Uri) -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.settings_import_export_title))
    SettingsImport(
        importGeoDataState = importGeoDataState,
        importTripDataState = importTripDataState,
        onSelectLocationFile = onSelectLocationFile,
        onSelectTripFile = onSelectTripFile
    )
}

@Preview
@Composable
fun SettingsImportExportSuccessPreview() {
    Column {
        SettingsImportExport(ImportState.ImportStarted.Success, ImportState.ImportStarted.Success, {}, {})
    }
}

@Preview
@Composable
fun SettingsImportExportLoadingsPreview() {
    Column {
        SettingsImportExport(ImportState.ImportStarted.Loading, ImportState.ImportStarted.Success, {}, {})
    }
}

@Preview
@Composable
fun SettingsImportExportErrorPreview() {
    Column {
        SettingsImportExport(ImportState.ImportStarted.Error, ImportState.ImportStarted.Success, {}, {})
    }
}

@Preview
@Composable
fun SettingsImportExportReadyPreview() {
    Column {
        SettingsImportExport(ImportState.Ready, ImportState.ImportStarted.Success, {}, {})
    }
}
