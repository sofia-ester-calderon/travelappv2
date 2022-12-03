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
                onChangeFromLongitude = { tripViewModel.updateFromLongitude(it) },
                onCalculateFromLatLong = { tripViewModel.onFromCalculateLatLong() },
                onChangeToCountry = { tripViewModel.updateToCountry(it) },
                onChangeToCity = { tripViewModel.updateToCity(it) },
                onChangeToLatitude = { tripViewModel.updateToLatitude(it) },
                onChangeToLongitude = { tripViewModel.updateToLongitude(it) },
                onCalculateToLatLong = { tripViewModel.onToCalculateLatLong() },
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
    onCalculateFromLatLong: () -> Unit,
    onChangeToCountry: (String) -> Unit,
    onChangeToCity: (String) -> Unit,
    onChangeToLatitude: (String) -> Unit,
    onChangeToLongitude: (String) -> Unit,
    onCalculateToLatLong: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TripOptions(
            tripType = tripUiState.tripType,
            onChangeTripType = onChangeTripType,
        )
        BigLabel(text = stringResource(id = R.string.trip_label_from))
        CountryCity(
            country = tripUiState.fromCountry,
            city = tripUiState.fromCity,
            latitude = tripUiState.fromLatitudeText,
            longitude = tripUiState.fromLongitudeText,
            latLongDbError = tripUiState.isFromLatLongDbError,
            onChangeCountry = onChangeFromCountry,
            onChangeCity = onChangeFromCity,
            onChangeLatitude = onChangeFromLatitude,
            onChangeLongitude = onChangeFromLongitude,
            onCalculateLatLong = onCalculateFromLatLong,
        )
        BigLabel(text = stringResource(id = R.string.trip_label_to))
        CountryCity(
            country = tripUiState.toCountry,
            city = tripUiState.toCity,
            latitude = tripUiState.toLatitudeText,
            longitude = tripUiState.toLongitudeText,
            latLongDbError = tripUiState.isToLatLongDbError,
            onChangeCountry = onChangeToCountry,
            onChangeCity = onChangeToCity,
            onChangeLatitude = onChangeToLatitude,
            onChangeLongitude = onChangeToLongitude,
            onCalculateLatLong = onCalculateToLatLong,
        )
    }
}

@Composable
private fun BigLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview
@Composable
fun NewTripPreview() {
    TripContent(
        tripUiState = TripUiState(),
        {},{},{},{},{},{},{},{},{},{},{},
    )
}