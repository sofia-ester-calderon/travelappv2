package com.sucaldo.travelappv2.ui.settings

import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.FieldErrorType

data class SettingsUiState(
    val homeCountry: String = "",
    val homeCity: String = "",
    val homeLocationErrorType: FieldErrorType = FieldErrorType.NONE,
    val homeLocationSaveSuccessful: Boolean = false,
    val readLoc: CityLocation? = null,
)