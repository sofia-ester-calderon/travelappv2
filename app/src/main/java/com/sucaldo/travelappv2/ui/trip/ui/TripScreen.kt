package com.sucaldo.travelappv2.ui.trip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
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
        stringResource(id = R.string.title_edit_trip) //TODO edit trip
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
                tripViewModel = tripViewModel,
            )
        }
    }
}

@Composable
fun TripContent(
    tripUiState: TripUiState,
    tripViewModel: TripViewModel,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TripOptions(
            tripType = tripUiState.tripType,
            onChangeTripType = { tripViewModel.updateTripType(it) },
        )
        BigLabel(text = stringResource(id = R.string.trip_label_from))
        CountryCity(
            country = tripUiState.fromCountry,
            city = tripUiState.fromCity,
            latitude = tripUiState.fromLatitudeText,
            longitude = tripUiState.fromLongitudeText,
            latLongDbError = tripUiState.isFromLatLongDbError,
            onChangeCountry = { tripViewModel.updateFromCountry(it) },
            onChangeCity = { tripViewModel.updateFromCity(it) },
            onChangeLatitude = { tripViewModel.updateFromLatitude(it) },
            onChangeLongitude = { tripViewModel.updateFromLongitude(it) },
            onCalculateLatLong = { tripViewModel.onFromCalculateLatLong() },
        )
        BigLabel(text = stringResource(id = R.string.trip_label_to))
        CountryCity(
            country = tripUiState.toCountry,
            city = tripUiState.toCity,
            latitude = tripUiState.toLatitudeText,
            longitude = tripUiState.toLongitudeText,
            latLongDbError = tripUiState.isToLatLongDbError,
            onChangeCountry = { tripViewModel.updateToCountry(it) },
            onChangeCity = { tripViewModel.updateToCity(it) },
            onChangeLatitude = { tripViewModel.updateToLatitude(it) },
            onChangeLongitude = { tripViewModel.updateToLongitude(it) },
            onCalculateLatLong = { tripViewModel.onToCalculateLatLong() },
        )
        BigLabel(text = stringResource(id = R.string.trip_label_dates))
        TripDate(
            startDate = tripUiState.startDate,
            endDate = tripUiState.endDate,
            onChangeStartDate =  { tripViewModel.updateStartDate(it) },
            onChangeEndDate =  { tripViewModel.updateEndDate(it) },
        )
    }
}

@Composable
private fun BigLabel(text: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.height(8.dp))
}
