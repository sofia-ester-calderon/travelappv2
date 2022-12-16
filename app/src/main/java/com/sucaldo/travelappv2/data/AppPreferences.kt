package com.sucaldo.travelappv2.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class AppPreferences(private val context: Context, databaseHelper: DatabaseHelper) {
    private val myDB: DatabaseHelper = databaseHelper

    suspend fun storeCountrySelection(country: String) {
        context.dataStore.edit { settings ->
            settings[COUNTRY_KEY] = country
        }
    }

    suspend fun storeCitySelection(city: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
        }
    }

    private suspend fun getStoredCountry(): String {
        val stringFlow = context.dataStore.data.map { preferences ->
            preferences[COUNTRY_KEY]
        }
        return stringFlow.first() ?: ""
    }

    private suspend fun getStoredCity(): String {
        val stringFlow = context.dataStore.data.map { preferences ->
            preferences[CITY_KEY]
        }
        return stringFlow.first() ?: ""
    }

    suspend fun getSavedHomeLocation(): CityLocation? {
        val country = getStoredCountry()
        val city = getStoredCity()
        return myDB.getLocationOfCity(country, city)
    }

    companion object {
        private val COUNTRY_KEY = stringPreferencesKey("key_home_country")
        private val CITY_KEY = stringPreferencesKey("key_home_city")

        val Context.dataStore by preferencesDataStore(name = "travel_app")
    }
}