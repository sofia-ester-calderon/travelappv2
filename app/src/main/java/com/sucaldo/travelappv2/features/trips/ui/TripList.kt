package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.features.trips.TripYear
import com.sucaldo.travelappv2.util.formatDate

@Composable
fun TripList(
    years: List<TripYear>,
    onExpandYear: (Int) -> Unit,
    onClickOnTrip: (Trip) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        years.forEach {
            val expanded = it.trips != null
            item {
                YearRow(year = it.year, expanded = expanded, onExpandYear = onExpandYear)
            }
            if (expanded) {
                items(items = it.trips!!) { trip ->
                    TripRow(trip, onClickOnTrip)
                }
            }
        }
    }
}

@Composable
private fun YearRow(year: Int, expanded: Boolean, onExpandYear: (Int) -> Unit) {
    Surface(elevation = 9.dp) {
        Row(
            Modifier
                .height(32.dp)
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth()
                .clickable {
                    onExpandYear(year)
                },
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = year.toString())
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = year.toString(),
                modifier = Modifier.rotate(
                    if (expanded)
                        180f
                    else
                        360f
                )
            )
        }

    }
}

@Composable
private fun TripRow(trip: Trip, onClickOnTrip: (Trip) -> Unit) {
    Row(
        Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .clickable { onClickOnTrip(trip) }
    ) {
        Text(text = formatDate(trip.startDate), modifier = Modifier.weight(1f))
        Text(text = trip.fromCity, modifier = Modifier.weight(1f))
        Text(text = trip.toCity, modifier = Modifier.weight(1f))
    }
}