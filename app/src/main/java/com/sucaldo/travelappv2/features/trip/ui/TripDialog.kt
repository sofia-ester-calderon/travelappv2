package com.sucaldo.travelappv2.features.trip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.TravelAppToast
import com.sucaldo.travelappv2.features.trip.TripDialogState

@Composable
fun TripDialog(
    tripDialogState: TripDialogState,
    onGoToMyTrips: (NavController) -> Unit,
    onAddAnotherTrip: () -> Unit,
    onAddNextStop: () -> Unit,
    onCompleteTrip: (NavController) -> Unit,
    navController: NavController
) {
    when (tripDialogState) {
        TripDialogState.NONE -> {}
        TripDialogState.EDIT_SUCCESS -> TravelAppToast(stringResource(id = R.string.trip_dialog_edit_success))
        TripDialogState.SAVE_ERROR -> TravelAppToast(stringResource(id = R.string.trip_dialog_save_error))
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
            onSelectOption2 = { onCompleteTrip(navController) },
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