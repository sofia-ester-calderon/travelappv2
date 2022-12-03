package com.sucaldo.travelappv2.ui.trip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.TripType
import com.sucaldo.travelappv2.ui.common.TopBar
import com.sucaldo.travelappv2.ui.trip.TripUiState
import com.sucaldo.travelappv2.ui.trip.TripViewModel

@Composable
fun TripScreen(
    navController: NavController,
    tripId: String?,
    tripViewModel: TripViewModel = viewModel(),
) {
    val tripUiState by tripViewModel.uiState.collectAsState()
    val title = if (tripId == null) {
        stringResource(id = R.string.title_new_trip)
    } else {
        stringResource(id = R.string.title_edit_trip)
    }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = title,
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            TripContent(
                tripUiState = tripUiState,
                onChangeTripType = { tripViewModel.updateTripType(it) },
                onChangeFromCountry = { tripViewModel.updateFromCountry(it) },
                onChangeFromCity = { tripViewModel.updateFromCity(it) },
                onChangeFromLatitude = { tripViewModel.updateFromLatitude(it) },
                onChangeFromLongitude = { tripViewModel.updateLongitude(it) },
                onCalculateLatLong = { tripViewModel.onFromCalculateLatLong() },
            )
        }
    }
}

@Composable
fun TripContent(
    tripUiState: TripUiState,
    onChangeTripType: (TripType) -> Unit,
    onChangeFromCountry: (String) -> Unit,
    onChangeFromCity: (String) -> Unit,
    onChangeFromLatitude: (String) -> Unit,
    onChangeFromLongitude: (String) -> Unit,
    onCalculateLatLong: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TripOptions(
            tripType = tripUiState.tripType,
            onChangeTripType = onChangeTripType,
        )
        Text(
            text = stringResource(id = R.string.trip_label_from),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        CountryCity(
            tripUiState = tripUiState,
            onChangeFromCountry = onChangeFromCountry,
            onChangeFromCity = onChangeFromCity,
            onChangeFromLatitude = onChangeFromLatitude,
            onChangeFromLongitude = onChangeFromLongitude,
            onCalculateLatLong = onCalculateLatLong,
        )
    }
}

@Preview
@Composable
fun NewTripPreview() {
    TripContent(
        tripUiState = TripUiState(),
        onChangeTripType = {},
        onCalculateLatLong = {},
        onChangeFromLongitude = {},
        onChangeFromLatitude = {},
        onChangeFromCity = {},
        onChangeFromCountry = {},
    )
}