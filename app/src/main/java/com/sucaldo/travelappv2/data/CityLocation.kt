package com.sucaldo.travelappv2.data

import android.database.Cursor

data class CityLocation(
    var city: String,
    val country: String,
    val latitude: Float = 0f,
    val longitude: Float = 0f,
    val id: Int = 0,
) {
    constructor(data: Cursor) : this(
        city = data.getString(0),
        country = data.getString(1),
        latitude = data.getFloat(2),
        longitude = data.getFloat(3),
        id = data.getInt(4),
    )
}