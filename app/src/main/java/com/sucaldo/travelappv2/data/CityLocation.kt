package com.sucaldo.travelappv2.data

import android.database.Cursor

class CityLocation {
    val city: String
    val country: String
    val latitude: Float
    val longitude: Float
    var id: Int? = null

    constructor(data: Cursor) {
        city = data.getString(0)
        country = data.getString(1)
        latitude = data.getFloat(2)
        longitude = data.getFloat(3)
        id = data.getInt(4)
    }

    constructor(country: String, city: String, latitude: Float, longitude: Float) {
        this.country = country
        this.city = city
        this.latitude = latitude
        this.longitude = longitude
    }
}