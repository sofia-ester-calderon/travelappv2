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
import com.sucaldo.travelappv2.ui.trip.TripErrorType
import com.sucaldo.travelappv2.ui.trip.TripUiState
import com.sucaldo.travelappv2.ui.trip.TripUiType
import com.sucaldo.travelappv2.ui.trip.TripViewModel

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
            onCalculateLatLong = { tripViewModel.onCalculateFromLatLong() },
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
            errorText = getErrorText(tripErrorType = tripUiState.descriptionErrorType)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { tripViewModel.saveTrip(navController) }) {
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



@Composable
fun getErrorText(tripErrorType: TripErrorType): String? {
    return when(tripErrorType) {
        TripErrorType.EMPTY -> stringResource(id = R.string.trip_error_empty)
        TripErrorType.INVALID_DATE_FORMAT -> stringResource(id = R.string.trip_error_invalid_date_format)
        TripErrorType.INVALID_END_DATE -> stringResource(id = R.string.trip_error_invalid_end_date)
        TripErrorType.INVALID_START_DATE -> stringResource(id = R.string.trip_error_invalid_start_date)
        TripErrorType.LAT_LONG_DB -> stringResource(id = R.string.trip_error_db)
        TripErrorType.INVALID_CHARS -> stringResource(id = R.string.trip_error_invalid_chars)
        TripErrorType.NONE -> null
    }
}

