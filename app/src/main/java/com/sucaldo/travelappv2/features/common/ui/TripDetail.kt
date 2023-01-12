package com.sucaldo.travelappv2.features.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.util.formatDate
import java.util.*

@Composable
fun TripDetail(trip: Trip) {
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
