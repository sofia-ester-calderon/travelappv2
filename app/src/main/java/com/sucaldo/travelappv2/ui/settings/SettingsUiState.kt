package com.sucaldo.travelappv2.ui.settings

import com.sucaldo.travelappv2.data.FieldErrorType

data class SettingsUiState(
    val homeCountry: String = "",
    val homeCity: String = "",
    val homeLocationErrorType: FieldErrorType = FieldErrorType.NONE,
    val homeLocationSaveSuccessful: Boolean = false,
    val importGeoDataState: ImportState = ImportState.READY,
    val importTripState: ImportState = ImportState.READY,
)

enum class ImportState {
    NOT_IMPORTABLE,
    READY,
    LOADING,
    IMPORT_SUCCESS,
    IMPORT_FAIL,
}