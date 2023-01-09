package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
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
import com.sucaldo.travelappv2.features.common.ui.TopBar
import com.sucaldo.travelappv2.features.trips.TripYear
import com.sucaldo.travelappv2.features.trips.TripsViewModel

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
            item {
                YearRow(year = it.year, expanded = it.expanded, onExpandYear = onExpandYear)
            }
            if (it.expanded) {
                item {
                    Row {
                        Text(text = "TRIPS")
                    }
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
            contentDescription = "Open",
            modifier = Modifier.rotate(
                if (expanded)
                    180f
                else
                    360f
            )
        )
    }
}

@Preview
@Composable
fun TripsPreview() {
    TripsContent(
        years = listOf(
            TripYear(2008, false),
            TripYear(2009, false),
            TripYear(2010, true),
            TripYear(2011, false)
        ), {}
    )
}