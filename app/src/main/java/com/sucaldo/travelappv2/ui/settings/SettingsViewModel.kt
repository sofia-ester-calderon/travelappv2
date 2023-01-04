package com.sucaldo.travelappv2.ui.settings

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.AppPreferences
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.db.CsvHelper
import com.sucaldo.travelappv2.db.DatabaseHelper2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper2
    private val appPreferences: AppPreferences
    private val contentResolver: ContentResolver
    private val csvHelper: CsvHelper


    init {
        myDb = DatabaseHelper2(application.applicationContext)
        appPreferences = AppPreferences(application.applicationContext, myDb)
        contentResolver = application.applicationContext.contentResolver
        csvHelper = CsvHelper(myDb)
        runBlocking {
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
            runBlocking {
                appPreferences.storeHomeLocation(homeCountry, homeCity)
                _uiState.value = _uiState.value.copy(homeLocationSaveSuccessful = true)
            }
        }
    }

    fun readCsv(uri: Uri) {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            csvHelper.readCityLocationsCsvFile(inputStream)
        }
    }

    fun getGeoData() {
        val locationOfCity = myDb.getLocationOfCity("Australia", "Sydney")
        _uiState.value = _uiState.value.copy(readLoc = locationOfCity)
    }
}
