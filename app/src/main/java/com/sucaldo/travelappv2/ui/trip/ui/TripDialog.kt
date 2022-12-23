package com.sucaldo.travelappv2.ui.trip.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.trip.TripDialogState

@Composable
fun TripDialog(tripDialogState: TripDialogState) {
    when(tripDialogState) {
        TripDialogState.NONE -> {}
        TripDialogState.EDIT_SUCCESS -> TripToastContent(stringResource(id = R.string.trip_dialog_edit_success))
    }
}

@Composable
private fun TripDialogContent() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Column(
            Modifier
                .background(Color.White)
                .padding(16.dp)) {
            Text(text = "I am a dialog")
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Click")
            }
        }
    }
}

@Composable
private fun TripToastContent(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}