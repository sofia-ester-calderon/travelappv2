package com.sucaldo.travelappv2.ui.settings.ui

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.ui.common.BigLabel

@Composable
fun SettingsImportExport(
    onOpenFile: (Uri) -> Unit,
    onGetData: () -> Unit,
    location: CityLocation?,
) {
    BigLabel(text = stringResource(id = R.string.settings_import_export_title))

    var pickedFileUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            pickedFileUri = it.data?.data
        }
    pickedFileUri?.let {
        onOpenFile(it)
    }
    Button(
        onClick = {
            val intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    .apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }
            launcher.launch(intent)
        }
    ) {
        Text(stringResource(id = R.string.settings_import_export_import_locations))
    }
    Button(onClick = onGetData) {
        Text(text = "Get Geo Data")
    }
    location?.let {
        Text(text = "Country: ${location.country}, city: ${location.city}, latitude: ${location.latitude}, longitude: ${location.longitude}")
    }
}