package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.Trip
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
                showTripDetails = tripsUiState.showTripDetails,
                tripDetail = tripsUiState.tripDetail,
                onExpandYear = { tripsViewModel.onExpandYear(it) },
                onOpenTripDetails = { tripsViewModel.openTripDetails(it) },
                onCloseTripDetails = { tripsViewModel.closeTripDetails() },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TripsContent(
    years: List<TripYear>,
    showTripDetails: Boolean,
    tripDetail: Trip?,
    onExpandYear: (Int) -> Unit,
    onOpenTripDetails: (Trip) -> Unit,
    onCloseTripDetails: () -> Unit,
) {
    val bottomDrawerState = remember {
        BottomDrawerState(
            BottomDrawerValue.Closed,
            confirmStateChange = { bottomDrawerValue ->
                if (bottomDrawerValue == BottomDrawerValue.Closed) {
                    onCloseTripDetails()
                }
                true
            }
        )
    }
    BottomDrawerLaunchedEffect(state = bottomDrawerState, isBottomDrawerOpen = showTripDetails)

    BottomDrawer(drawerState = bottomDrawerState, gesturesEnabled = true, drawerContent = {
        if (tripDetail != null) {
            TripDetail(trip = tripDetail)
        } else {
            Text(text = "No Trip Selected")
        }
    }) {
        TripList(years, onExpandYear, onOpenTripDetails)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomDrawerLaunchedEffect(
    state: BottomDrawerState,
    isBottomDrawerOpen: Boolean,
) {
    LaunchedEffect(isBottomDrawerOpen) {
        if (isBottomDrawerOpen) {
            state.open()
        } else {
            state.close()
        }
    }
}
