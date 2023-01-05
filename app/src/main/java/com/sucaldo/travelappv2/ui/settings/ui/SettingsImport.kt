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
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.settings.ImportState
import com.sucaldo.travelappv2.ui.trip.ui.ErrorText

@Composable
fun SettingsImport(
    importGeoDataState: ImportState,
    onSelectLocationFile: (Uri) -> Unit,
) {
    ImportGeoData(importGeoDataState, onSelectLocationFile)
}

@Composable
private fun ImportGeoData(
    importGeoDataState: ImportState,
    onSelectLocationFile: (Uri) -> Unit,
) {
    ImportData(
        label = stringResource(id = R.string.settings_import_locations),
        onSelectFile = onSelectLocationFile,
        enabled = importGeoDataState != ImportState.NOT_IMPORTABLE,
    ) {
        ImportGeoDataInfo(importGeoDataState)
    }
}

@Composable
private fun ImportData(
    label: String,
    onSelectFile: (Uri) -> Unit,
    enabled: Boolean = true,
    info: @Composable() () -> Unit
) {
    var pickedFileUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            pickedFileUri = it.data?.data
        }
    pickedFileUri?.let {
        onSelectFile(it)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            enabled = enabled,
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
            Text(label)
        }
        Spacer(modifier = Modifier.width(8.dp))
        info()
    }
}

@Composable
private fun ImportGeoDataInfo(importGeoDataState: ImportState) {
    Column(verticalArrangement = Arrangement.Bottom) {
        when (importGeoDataState) {
            ImportState.READY -> ErrorText(text = stringResource(id = R.string.settings_import_ready))
            ImportState.IMPORT_SUCCESS -> Icon(
                Icons.Default.Check,
                contentDescription = stringResource(id = R.string.settings_import_success)
            )
            ImportState.IMPORT_FAIL -> Icon(
                Icons.Default.Close,
                contentDescription = stringResource(id = R.string.settings_import_error)
            )
            ImportState.LOADING -> CircularProgressIndicator(Modifier.size(24.dp))
            ImportState.NOT_IMPORTABLE -> ErrorText(text = stringResource(id = R.string.settings_import_not_possible))
        }
    }
}