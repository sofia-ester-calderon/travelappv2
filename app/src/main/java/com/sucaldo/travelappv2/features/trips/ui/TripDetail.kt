package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.util.formatDate
import java.util.*
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.common.ui.Routes

@Composable
fun TripDetail(navController: NavController, trip: Trip, onDeleteTrip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DateText(startDate = trip.startDate, endDate = trip.endDate)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.trips_from_to, trip.fromCity, trip.toCity),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.trips_distance, trip.distance.toString()))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = trip.description)
    }
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

@Composable
private fun DateText(startDate: Date, endDate: Date?) {
    if (endDate == null) {
        Text(text = formatDate(startDate))
    } else {
        Text(
            text = stringResource(
                id = R.string.trips_dates,
                formatDate(startDate),
                formatDate(endDate)
            )
        )
    }
}
