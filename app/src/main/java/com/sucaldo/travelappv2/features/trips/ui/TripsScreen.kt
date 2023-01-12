package com.sucaldo.travelappv2.features.trips.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
                navController = navController,
                years = tripsUiState.tripYears,
                tripDetail = tripsUiState.tripDetail,
                showDeleteDialog = tripsUiState.showDeleteDialog,
                onExpandYear = { tripsViewModel.onToggleYear(it) },
                onOpenTripDetails = { tripsViewModel.openTripDetails(it) },
                onCloseTripDetails = { tripsViewModel.closeTripDetails() },
                onHideDeleteDialog = { tripsViewModel.hideDeleteDialog() },
                onClickDeleteTrip = { tripsViewModel.clickDeleteTrip() },
                onDeleteTrip = { tripsViewModel.confirmDeleteTrip() },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TripsContent(
    navController: NavController,
    years: List<TripYear>,
    tripDetail: Trip?,
    showDeleteDialog: Boolean,
    onExpandYear: (Int) -> Unit,
    onOpenTripDetails: (Trip) -> Unit,
    onCloseTripDetails: () -> Unit,
    onHideDeleteDialog: () -> Unit,
    onClickDeleteTrip: () -> Unit,
    onDeleteTrip: () -> Unit,
) {
    if (showDeleteDialog) {
        TripDeleteDialog(onHideDialog = onHideDeleteDialog, onDeleteTrip = onDeleteTrip)
    }
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
    BottomDrawerLaunchedEffect(state = bottomDrawerState, isBottomDrawerOpen = tripDetail != null)

    BottomDrawer(drawerState = bottomDrawerState, gesturesEnabled = true, drawerContent = {
        if (tripDetail != null) {
            TripListDetail(navController = navController, trip = tripDetail, onDeleteTrip = onClickDeleteTrip)
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

@Composable
private fun TripDeleteDialog(onHideDialog: () -> Unit, onDeleteTrip: () -> Unit) {
    Dialog(onDismissRequest = onHideDialog) {
        Column(
            Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.trips_delete_confirm))
            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    onDeleteTrip()
                }) {
                    Text(text = stringResource(id = R.string.common_yes))
                }
                Button(onClick = onHideDialog) {
                    Text(text = stringResource(id = R.string.common_no))
                }
            }
        }
    }
}
