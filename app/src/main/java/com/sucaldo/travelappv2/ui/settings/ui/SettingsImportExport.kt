package com.sucaldo.travelappv2.ui.settings.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.common.BigLabel
import com.sucaldo.travelappv2.ui.settings.ImportState

@Composable
fun SettingsImportExport(
    importGeoDataState: ImportState,
    onSelectLocationFile: (Uri) -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.settings_import_export_title))
    SettingsImport(importGeoDataState = importGeoDataState, onSelectLocationFile = onSelectLocationFile)
}

@Preview
@Composable
fun SettingsImportExportSuccessPreview() {
    Column {
        SettingsImportExport(ImportState.IMPORT_SUCCESS, {})
    }
}

@Preview
@Composable
fun SettingsImportExportLoadingsPreview() {
    Column {
        SettingsImportExport(ImportState.LOADING, {})
    }
}

@Preview
@Composable
fun SettingsImportExportErrorPreview() {
    Column {
        SettingsImportExport(ImportState.IMPORT_FAIL, {})
    }
}

@Preview
@Composable
fun SettingsImportExportReadyPreview() {
    Column {
        SettingsImportExport(ImportState.READY, {})
    }
}
