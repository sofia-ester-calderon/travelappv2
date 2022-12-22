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
            latLongError = tripUiState.fromLatLongErrorType,
            countryError = tripUiState.fromCountryErrorType,
            cityError = tripUiState.fromCityErrorType,
            countries = tripUiState.countries,
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
            latLongError = tripUiState.toLatLongDbErrorType,
            countryError = tripUiState.toCountryErrorType,
            cityError = tripUiState.toCityErrorType,
            countries = tripUiState.countries,
            onChangeCountry = { tripViewModel.updateToCountry(it) },
            onChangeCity = { tripViewModel.updateToCity(it) },
            onChangeLatitude = { tripViewModel.updateToLatitude(it) },
            onChangeLongitude = { tripViewModel.updateToLongitude(it) },
            onCalculateLatLong = { tripViewModel.onToCalculateLatLong() },
        )
        Spacer(modifier = Modifier.height(32.dp))
        TripDate(
            startDate = tripUiState.startDate,
            endDate = tripUiState.endDate,
            isEndDateEnabled = tripUiState.tripType != TripType.ONE_WAY,
            startDateError = tripUiState.startDateErrorType,
            endDateError = tripUiState.endDateErrorType,
            onChangeStartDate =  { tripViewModel.updateStartDate(it) },
            onChangeEndDate =  { tripViewModel.updateEndDate(it) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = tripUiState.description,
            onValueChange = { tripViewModel.updateDescription(it) },
            singleLine = true,
            label = { Text(stringResource(id = R.string.trip_label_description)) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { tripViewModel.saveTrip() }) {
                Text(text = "Save")
            }
        }
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
