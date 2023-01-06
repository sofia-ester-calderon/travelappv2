package com.sucaldo.travelappv2.features.settings.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.common.ui.InfoText
import com.sucaldo.travelappv2.features.settings.ImportState

@Composable
fun SettingsImportExport(
    importGeoDataState: ImportState,
    importTripDataState: ImportState,
    exportDataState: ImportState,
    onSelectLocationFile: (Uri) -> Unit,
    onSelectTripFile: (Uri) -> Unit,
    onExportData: () -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.settings_import_export_title))
    SettingsImport(
        importGeoDataState = importGeoDataState,
        importTripDataState = importTripDataState,
        onSelectLocationFile = onSelectLocationFile,
        onSelectTripFile = onSelectTripFile
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            modifier = Modifier.width(160.dp),
            onClick = onExportData
        ) {
            Text(stringResource(id = R.string.settings_export_title))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.Bottom) {
            when (exportDataState) {
                ImportState.Ready -> InfoText(text = stringResource(id = R.string.settings_export_ready))
                is ImportState.ImportStarted -> LoadingIcons(loadingState = exportDataState)
                else -> {}
            }
        }
    }
}

@Composable
fun LoadingIcons(loadingState: ImportState.ImportStarted) {
    when (loadingState) {
        ImportState.ImportStarted.Success -> Icon(
            Icons.Default.Check,
            contentDescription = stringResource(id = R.string.settings_import_success)
        )
        ImportState.ImportStarted.Error -> Icon(
            Icons.Default.Close,
            contentDescription = stringResource(id = R.string.settings_import_error)
        )
        ImportState.ImportStarted.Loading -> CircularProgressIndicator(Modifier.size(24.dp))
    }
}

@Preview
@Composable
fun SettingsImportExportSuccessPreview() {
    Column {
        SettingsImportExport(
            ImportState.ImportStarted.Success,
            ImportState.ImportStarted.Success,
            ImportState.Ready,
            {}, {}, {})
    }
}
