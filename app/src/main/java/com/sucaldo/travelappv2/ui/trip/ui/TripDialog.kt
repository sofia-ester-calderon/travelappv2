package com.sucaldo.travelappv2.ui.trip.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.trip.TripDialogState

@Composable
fun TripDialog(
    tripDialogState: TripDialogState,
    onGoToMyTrips: (NavController) -> Unit,
    onAddAnotherTrip: () -> Unit,
    onAddNextStop: () -> Unit,
    onCompleteTrip: () -> Unit,
    navController: NavController
) {
    when (tripDialogState) {
        TripDialogState.NONE -> {}
        TripDialogState.EDIT_SUCCESS -> TripToastContent(stringResource(id = R.string.trip_dialog_edit_success))
        TripDialogState.SAVE_ERROR -> TripToastContent(stringResource(id = R.string.trip_dialog_save_error))
        TripDialogState.SIMPLE_TRIP -> TripDialogContent(
            title = stringResource(id = R.string.trip_dialog_simple_title),
            option1Text = stringResource(id = R.string.trip_dialog_simple_option1),
            option2Text = stringResource(id = R.string.trip_dialog_simple_option2),
            onSelectOption1 = { onGoToMyTrips(navController) },
            onSelectOption2 = onAddAnotherTrip,
        )
        TripDialogState.MULTI_TRIP -> TripDialogContent(
            title = stringResource(id = R.string.trip_dialog_multi_title),
            option1Text = stringResource(id = R.string.trip_dialog_multi_option1),
            option2Text = stringResource(id = R.string.trip_dialog_multi_option2),
            onSelectOption1 = onAddNextStop,
            onSelectOption2 = onCompleteTrip,
        )
    }
}

@Composable
private fun TripDialogContent(
    title: String,
    option1Text: String,
    option2Text: String,
    onSelectOption1: () -> Unit,
    onSelectOption2: () -> Unit,
) {
    Dialog(onDismissRequest = {}) {
        Column(
            Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.trip_dialog_saved_content))
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = onSelectOption1) {
                    Text(text = option1Text)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onSelectOption2) {
                    Text(text = option2Text)
                }
            }
        }
    }
}

@Composable
private fun TripToastContent(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview
@Composable
fun TripDialogContentPreview() {
    TripDialogContent(
        title = stringResource(id = R.string.trip_dialog_simple_title),
        option1Text = stringResource(id = R.string.trip_dialog_simple_option1),
        option2Text = stringResource(id = R.string.trip_dialog_simple_option2),
        {}, {}
    )
}