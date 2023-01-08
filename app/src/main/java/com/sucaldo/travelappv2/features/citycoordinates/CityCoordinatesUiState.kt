package com.sucaldo.travelappv2.features.citycoordinates

import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.FieldErrorType

data class CityCoordinatesUiState(
    val country: String = "",
    val city: String = "",
    val countryErrorType: FieldErrorType = FieldErrorType.NONE,
    val cityErrorType: FieldErrorType = FieldErrorType.NONE,
    val cityCoordinates: List<CityLocation> = listOf(),
)
