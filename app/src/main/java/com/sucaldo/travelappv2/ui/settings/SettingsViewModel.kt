package com.sucaldo.travelappv2.ui.settings

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
import com.sucaldo.travelappv2.ui.settings.ImportGeoDataState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

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
            _uiState.update { it.copy(importState = NOT_IMPORTABLE) }
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
            runBlocking {
                appPreferences.storeHomeLocation(homeCountry, homeCity)
                _uiState.value = _uiState.value.copy(homeLocationSaveSuccessful = true)
            }
        }
    }

    fun importLocationDataFromCsv(uri: Uri) {
        _uiState.update { it.copy(importState = LOADING) }
        thread(start = true) {
            Thread.sleep(3000)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val isImportSuccessful = csvHelper.readCityLocationsCsvFile(inputStream)
                _uiState.update {
                    it.copy(
                        importState = if (isImportSuccessful) IMPORT_SUCCESS else IMPORT_FAIL
                    )
                }
            }
        }
    }

    fun startThread() {
        thread(start = true) {
            Log.d("VM", "New Thread")
            val locationOfCity = myDb.getLocationOfCity("Australia", "Sydney")
            Log.d("VM", locationOfCity?.city ?: "")
            _uiState.update {
                it.copy(
                    homeCountry = locationOfCity?.country ?: "",
                    homeCity = locationOfCity?.country ?: ""
                )
            }
        }
    }
}
