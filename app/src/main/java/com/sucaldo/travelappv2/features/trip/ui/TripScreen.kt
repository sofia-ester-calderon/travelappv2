package com.sucaldo.travelappv2.features.trip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.TripType
import com.sucaldo.travelappv2.features.common.ui.*
import com.sucaldo.travelappv2.features.trip.TripUiState
import com.sucaldo.travelappv2.features.trip.TripUiType
import com.sucaldo.travelappv2.features.trip.TripViewModel

@Composable
fun TripScreen(
    navController: NavController,
    tripId: String?,
    tripViewModel: TripViewModel = viewModel(),
) {
    val tripUiState by tripViewModel.uiState.collectAsState()

    TripDialog(
        tripDialogState = tripUiState.tripDialogState,
        onGoToMyTrips = { tripViewModel.onGoToMyTrips(navController) },
        onAddAnotherTrip = { tripViewModel.onAddAnotherTrip() },
        onAddNextStop = { tripViewModel.onAddNextStop() },
        onCompleteTrip = { tripViewModel.onCompleteStop(navController) },
        navController = navController,
    )
    val title = when(tripUiState.tripUiType) {
        TripUiType.NEW ->  stringResource(id = R.string.title_new_trip)
        TripUiType.EDIT -> stringResource(id = R.string.title_edit_trip)
        TripUiType.NEW_STOP -> stringResource(id = R.string.title_new_stop)
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
                navController = navController,
            )
        }
    }
}

@Composable
fun TripContent(
    tripUiState: TripUiState,
    tripViewModel: TripViewModel,
    navController: NavController,
    ) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (tripUiState.tripUiType != TripUiType.NEW_STOP) {
            TripOptions(
                tripType = tripUiState.tripType,
                onChangeTripType = { tripViewModel.updateTripType(it) },
            )
        }
        BigLabel(text = stringResource(id = R.string.trip_label_from))
        CountryCityLatLong(
            country = tripUiState.fromCountry,
            city = tripUiState.fromCity,
            latitude = tripUiState.fromLatitudeText,
            longitude = tripUiState.fromLongitudeText,
            latLongError = tripUiState.fromLatLongErrorType,
            countryError = tripUiState.fromCountryErrorType,
            cityError = tripUiState.fromCityErrorType,
            onChangeCountry = { tripViewModel.updateFromCountry(it) },
            onChangeCity = { tripViewModel.updateFromCity(it) },
            onChangeLatitude = { tripViewModel.updateFromLatitude(it) },
            onChangeLongitude = { tripViewModel.updateFromLongitude(it) },
            onCalculateLatLong = { tripViewModel.onCalculateFromLatLong() },
        )
        BigLabel(text = stringResource(id = R.string.trip_label_to))
        CountryCityLatLong(
            country = tripUiState.toCountry,
            city = tripUiState.toCity,
            latitude = tripUiState.toLatitudeText,
            longitude = tripUiState.toLongitudeText,
            latLongError = tripUiState.toLatLongDbErrorType,
            countryError = tripUiState.toCountryErrorType,
            cityError = tripUiState.toCityErrorType,
            onChangeCountry = { tripViewModel.updateToCountry(it) },
            onChangeCity = { tripViewModel.updateToCity(it) },
            onChangeLatitude = { tripViewModel.updateToLatitude(it) },
            onChangeLongitude = { tripViewModel.updateToLongitude(it) },
            onCalculateLatLong = { tripViewModel.onCalculateToLatLong() },
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
        TravelAppTextField(
            modifier = Modifier.fillMaxWidth(),
            value = tripUiState.description,
            onValueChange = { tripViewModel.updateDescription(it) },
            label = (stringResource(id = R.string.trip_label_description)),
            errorText = getErrorText(fieldErrorType = tripUiState.descriptionErrorType)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { tripViewModel.saveTrip(navController) }) {
                Text(text = stringResource(id = R.string.trip_button_save))
            }
        }
    }
}


