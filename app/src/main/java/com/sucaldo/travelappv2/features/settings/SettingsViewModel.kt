package com.sucaldo.travelappv2.features.settings

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sucaldo.travelappv2.data.AppPreferences
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.db.CsvHelper
import com.sucaldo.travelappv2.db.DatabaseHelper
import com.sucaldo.travelappv2.features.settings.ImportState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper
    private val appPreferences: AppPreferences
    private val contentResolver: ContentResolver
    private val csvHelper: CsvHelper

    init {
        myDb = DatabaseHelper(application.applicationContext)
        appPreferences = AppPreferences(application.applicationContext, myDb)
        contentResolver = application.applicationContext.contentResolver
        csvHelper = CsvHelper(myDb)
        if (!myDb.isCityLocTableEmpty) {
            _uiState.update { it.copy(importGeoDataState = DbPopulated) }
        }
        if (!myDb.isTripTableEmpty) {
            _uiState.update { it.copy(importTripState = DbPopulated) }
        }
        viewModelScope.launch {
            val savedHomeLocation = appPreferences.getSavedHomeLocation()
            _uiState.value = _uiState.value.copy(
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
            viewModelScope.launch {
                appPreferences.storeHomeLocation(homeCountry, homeCity)
                _uiState.value = _uiState.value.copy(homeLocationSaveSuccessful = true)
            }
        }
    }

    fun saveHomeCountry(country: String) {
        _uiState.update { it.copy(homeCountry = country) }
    }

    fun saveHomeCity(city: String) {
        _uiState.update { it.copy(homeCity = city) }
    }

    fun importLocationDataFromCsv(uri: Uri) {
        _uiState.update { it.copy(importGeoDataState = ImportStarted.Loading) }
        viewModelScope.launch {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val isImportSuccessful = csvHelper.readCityLocationsCsvFile(inputStream)
                _uiState.update {
                    it.copy(
                        importGeoDataState = if (isImportSuccessful) ImportStarted.Success else ImportStarted.Error
                    )
                }
            }
        }
    }

    fun importTripDataFromCsv(uri: Uri) {
        _uiState.update { it.copy(importTripState = ImportStarted.Loading) }
        viewModelScope.launch {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val isImportSuccessful = csvHelper.readTripsCsvFile(inputStream)
                _uiState.update {
                    it.copy(
                        importTripState = if (isImportSuccessful) ImportStarted.Success else ImportStarted.Error
                    )
                }
            }
        }
    }

    fun exportAllData() {
        _uiState.update { it.copy(exportDataState = ImportStarted.Loading) }
        viewModelScope.launch {
            try {
                csvHelper.writeTripsToCsv()
                csvHelper.writeCityLocationsToCsv()
                _uiState.update { it.copy(exportDataState = ImportStarted.Success) }
            } catch (e: Exception) {
                Log.e("Export data error", e.stackTraceToString())
                _uiState.update { it.copy(exportDataState = ImportStarted.Error) }
            }

        }

    }
}
