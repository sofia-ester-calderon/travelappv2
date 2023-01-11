package com.sucaldo.travelappv2.features.worldmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WorldMapViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(WorldMapUiState())
    val uiState: StateFlow<WorldMapUiState> = _uiState.asStateFlow()
    private val myDb: DatabaseHelper

    init {
        myDb = DatabaseHelper(application.applicationContext)
    }

    fun drawCircles(worldMapSize: WorldMapSize) {
        val circleLocations: List<CityLocation> = myDb.latitudeAndLongitudeOfVisitedCities
        val locationCircles: MutableList<LocationCircle> = arrayListOf()

        for (circleLocation in circleLocations) {
            locationCircles.add(
                LocationCircle(
                    offsetX = getLongitudePosition(circleLocation.longitude, worldMapSize.width),
                    offsetY = getLatitudePosition(circleLocation.latitude, worldMapSize.height),
                )
            )
        }
        _uiState.update { it.copy(locationCircles = locationCircles) }
    }

    private fun getLatitudePosition(latitude: Float, height: Float): Float {
        return latitude * height / 180
    }


    private fun getLongitudePosition(longitude: Float, width: Float): Float {
        return longitude * width / 360
    }
}