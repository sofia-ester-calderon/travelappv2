package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.features.common.ui.BigLabel
import com.sucaldo.travelappv2.features.common.ui.TripDetail

@Composable
fun StatisticsOverview(
    worldTimesTravelled: Float,
    numberOfVisitedCountries: Int,
    numberOfVisitedPlaces: Int,
    numberOfTrips: Int,
    randomTrip: Trip?,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp), verticalArrangement = Arrangement.Center) {
        StatisticsRow(label = "Number of trips", value = numberOfTrips.toString())
        StatisticsRow(label = "Countries visited", value = numberOfVisitedCountries.toString())
        StatisticsRow(label = "Places visited", value = numberOfVisitedPlaces.toString())
        StatisticsRow(label = "Travelled around world", value = "${"%.2f".format(worldTimesTravelled)} times")
        Spacer(modifier = Modifier.height(24.dp))
        Surface(elevation = 9.dp) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BigLabel(text = "Random Trip of the Day")
                    randomTrip?.let {
                        TripDetail(trip = it)
                    }
            }
        }
    }
}

@Composable
private fun StatisticsRow(label: String, value: String) {
    Row {
        Text(text = "$label: ")
        Text(text = value, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(8.dp))
}