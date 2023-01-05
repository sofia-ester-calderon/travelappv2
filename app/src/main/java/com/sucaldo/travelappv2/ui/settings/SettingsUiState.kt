package com.sucaldo.travelappv2.ui.settings

import com.sucaldo.travelappv2.data.FieldErrorType

data class SettingsUiState(
    val homeCountry: String = "",
    val homeCity: String = "",
    val homeLocationErrorType: FieldErrorType = FieldErrorType.NONE,
    val homeLocationSaveSuccessful: Boolean = false,
    val importGeoDataState: ImportState = ImportState.Ready,
    val importTripState: ImportState = ImportState.Ready,
)

sealed interface ImportState {
    object DbPopulated: ImportState
    object Ready: ImportState

    sealed class ImportStarted : ImportState {
        object Loading : ImportStarted()
        object Success : ImportStarted()
        object Error : ImportStarted()
    }
}
