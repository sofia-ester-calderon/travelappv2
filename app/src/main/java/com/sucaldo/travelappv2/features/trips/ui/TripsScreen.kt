package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.trips.TripYear
import com.sucaldo.travelappv2.features.trips.TripsViewModel
import com.sucaldo.travelappv2.util.formatDate

@Composable
fun TripsScreen(
    navController: NavController,
    tripsViewModel: TripsViewModel = viewModel()
) {
    val tripsUiState by tripsViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.title_trips)
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            TripsContent(
                years = tripsUiState.tripYears,
                onExpandYear = { tripsViewModel.onExpandYear(it) })
        }
    }
}

@Composable
private fun TripsContent(years: List<TripYear>, onExpandYear: (Int) -> Unit) {
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
                items(items = it.trips!!) { trip->
                    TripRow(trip)
                }
            }
        }
    }
}

@Composable
private fun YearRow(year: Int, expanded: Boolean, onExpandYear: (Int) -> Unit) {
    Row(
        Modifier
            .background(Color.LightGray)
            .height(32.dp)
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .clickable {
                onExpandYear(year)
            },
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically,
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

@Composable
private fun TripRow(trip: Trip) {
    Row(
        Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()) {
        Text(text = formatDate(trip.startDate), modifier = Modifier.weight(1f))
        Text(text = trip.fromCity, modifier = Modifier.weight(1f))
        Text(text = trip.toCity, modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun TripsPreview() {
    TripsContent(
        years = listOf(
            TripYear(2008, listOf()),
            TripYear(2009, null),
            TripYear(2010, listOf()),
            TripYear(2011, listOf())
        ), {}
    )
}