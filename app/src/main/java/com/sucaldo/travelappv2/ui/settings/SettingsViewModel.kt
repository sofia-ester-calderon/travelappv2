package com.sucaldo.travelappv2.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.AppPreferences
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper
    private val appPreferences: AppPreferences

    init {
        myDb = DatabaseHelper(application.applicationContext)
        appPreferences = AppPreferences(application.applicationContext, myDb)
        runBlocking {
            val savedHomeLocation = appPreferences.getSavedHomeLocation()
            _uiState.value = _uiState.value.copy(
                countries = myDb.countries,
                homeCountry = savedHomeLocation?.country ?: "",
                homeCity = savedHomeLocation?.city ?: "",
            )
        }
    }

    fun saveHomeLocation() {
        val homeCountry = uiState.value.homeCountry
        val homeCity = uiState.value.homeCity
        val cityCoordinates = myDb.getStoredCityCoordinates(homeCountry, homeCity)
        if (cityCoordinates.size == 0) {
            _uiState.value = _uiState.value.copy(homeLocationErrorType = FieldErrorType.LOCATION_DB)
        } else {
            runBlocking {
                appPreferences.storeHomeLocation(homeCountry, homeCity)
                _uiState.value = _uiState.value.copy(homeLocationSaveSuccessful = true)
            }
        }
    }
}
