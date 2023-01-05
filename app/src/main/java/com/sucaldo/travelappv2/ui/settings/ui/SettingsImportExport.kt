package com.sucaldo.travelappv2.ui.settings.ui

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.common.BigLabel
import com.sucaldo.travelappv2.ui.settings.ImportGeoDataState
import com.sucaldo.travelappv2.ui.trip.ui.ErrorText

@Composable
fun SettingsImportExport(
    importGeoDataState: ImportGeoDataState,
    onSelectLocationFile: (Uri) -> Unit,
) {
    BigLabel(text = stringResource(id = R.string.settings_import_export_title))

    var pickedFileUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            pickedFileUri = it.data?.data
        }
    pickedFileUri?.let {
        onSelectLocationFile(it)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            enabled = importGeoDataState != ImportGeoDataState.NOT_IMPORTABLE,
            onClick = {
                val intent =
                    Intent(
                        Intent.ACTION_OPEN_DOCUMENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                        .apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                        }
                launcher.launch(intent)
            }
        ) {
            Text(stringResource(id = R.string.settings_import_locations))
        }
        Spacer(modifier = Modifier.width(8.dp))
        ImportGeoDataInfo(importGeoDataState)
    }
}

@Composable
private fun ImportGeoDataInfo(importGeoDataState: ImportGeoDataState) {
    Column(verticalArrangement = Arrangement.Bottom) {
        when (importGeoDataState) {
            ImportGeoDataState.READY -> ErrorText(text = stringResource(id = R.string.settings_import_ready))
            ImportGeoDataState.IMPORT_SUCCESS -> Icon(
                Icons.Default.Check,
                contentDescription = stringResource(id = R.string.settings_import_success)
            )
            ImportGeoDataState.IMPORT_FAIL -> Icon(
                Icons.Default.Close,
                contentDescription = stringResource(id = R.string.settings_import_error)
            )
            ImportGeoDataState.LOADING -> CircularProgressIndicator(Modifier.size(24.dp))
            ImportGeoDataState.NOT_IMPORTABLE -> ErrorText(text = stringResource(id = R.string.settings_import_not_possible))
        }
    }
}

@Preview
@Composable
fun SettingsImportExportSuccessPreview() {
    Column {
        SettingsImportExport(ImportGeoDataState.IMPORT_SUCCESS, {})
    }
}

@Preview
@Composable
fun SettingsImportExportLoadingsPreview() {
    Column {
        SettingsImportExport(ImportGeoDataState.LOADING, {})
    }
}

@Preview
@Composable
fun SettingsImportExportErrorPreview() {
    Column {
        SettingsImportExport(ImportGeoDataState.IMPORT_FAIL, {})
    }
}

@Preview
@Composable
fun SettingsImportExportReadyPreview() {
    Column {
        SettingsImportExport(ImportGeoDataState.READY, {})
    }
}
