package com.sucaldo.travelappv2.features.worldmap

data class WorldMapUiState(
    val locationCircles: List<LocationCircle> = listOf(),
)

data class LocationCircle(
    val offsetX: Float,
    val offsetY: Float,
)

data class WorldMapSize(
    val height: Float,
    val width: Float,
)