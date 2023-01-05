package com.sucaldo.travelappv2.ui.settings

import com.sucaldo.travelappv2.data.FieldErrorType

data class SettingsUiState(
    val homeCountry: String = "",
    val homeCity: String = "",
    val homeLocationErrorType: FieldErrorType = FieldErrorType.NONE,
    val homeLocationSaveSuccessful: Boolean = false,
    val importState: ImportGeoDataState = ImportGeoDataState.READY,
)

enum class ImportGeoDataState {
    NOT_IMPORTABLE,
    READY,
    LOADING,
    IMPORT_SUCCESS,
    IMPORT_FAIL,
}