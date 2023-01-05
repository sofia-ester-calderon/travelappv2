package com.sucaldo.travelappv2.features.settings.ui

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
import com.sucaldo.travelappv2.features.common.ui.ErrorText
import com.sucaldo.travelappv2.features.common.ui.InfoText
import com.sucaldo.travelappv2.features.settings.ImportState

@Composable
fun SettingsImport(
    importGeoDataState: ImportState,
    importTripDataState: ImportState,
    onSelectLocationFile: (Uri) -> Unit,
    onSelectTripFile: (Uri) -> Unit,
) {
    ImportData(
        label = stringResource(id = R.string.settings_import_locations),
        onSelectFile = onSelectLocationFile,
        enabled = importGeoDataState != ImportState.DbPopulated && importGeoDataState != ImportState.ImportStarted.Success,
    ) {
        ImportGeoDataInfo(importGeoDataState)
    }
    ImportData(
        label = stringResource(id = R.string.settings_import_trips),
        onSelectFile = onSelectTripFile,
    ) {
        ImportTripDataInfo(importTripDataState)
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
            modifier = Modifier.width(160.dp),
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
            ImportState.Ready -> ErrorText(text = stringResource(id = R.string.settings_import_geo_ready))
            ImportState.DbPopulated -> InfoText(text = stringResource(id = R.string.settings_import_geo_not_possible))
            is ImportState.ImportStarted -> LoadingIcons(loadingState = importGeoDataState)
        }
    }
}

@Composable
private fun ImportTripDataInfo(importTripDataState: ImportState) {
    Column(verticalArrangement = Arrangement.Bottom) {
        when (importTripDataState) {
            ImportState.Ready -> InfoText(text = stringResource(id = R.string.settings_import_trip_ready))
            ImportState.DbPopulated -> ErrorText(text = stringResource(id = R.string.settings_import_trip_db_full))
            is ImportState.ImportStarted -> LoadingIcons(loadingState = importTripDataState)
        }
    }
}

@Composable
private fun LoadingIcons(loadingState: ImportState.ImportStarted) {
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