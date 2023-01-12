package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.features.common.ui.Routes
import com.sucaldo.travelappv2.features.common.ui.TripDetail

@Composable
fun TripListDetail(navController: NavController, trip: Trip, onDeleteTrip: () -> Unit) {
    TripDetail(trip = trip)
    TripActionButtons(navController = navController, tripId = trip.id!!, onDeleteTrip = onDeleteTrip)
}

@Composable
private fun TripActionButtons(navController: NavController, tripId: Int, onDeleteTrip: () -> Unit) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onDeleteTrip) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.trips_delete),
                    tint = Color.Red
                )
            }
            IconButton(onClick = {
                navController.navigate("${Routes.TRIP}?tripId=${tripId}")
            }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.title_edit_trip)
                )
            }
        }
    }
}
