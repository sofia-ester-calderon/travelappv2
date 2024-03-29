package com.sucaldo.travelappv2.util

import kotlin.math.atan2
import kotlin.math.sqrt

object DistanceCalculator {
    const val WORLD_CIRCUMFERENCE = 40075

    fun getDistanceFromLatLongInKms(lat1: Float, long1: Float, lat2: Float, long2: Float, isDoubleDistance: Boolean = false): Long {
        val radiusEarth = 6371
        val dLat = degToRadius(lat2 - lat1)
        val dLong = degToRadius(long2 - long1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(degToRadius(lat1)) * Math.cos(degToRadius(lat2)) *
                Math.sin(dLong / 2) * Math.sin(dLong / 2)
        val c = 2 * atan2(Math.sqrt(a), sqrt(1 - a))
        var d = radiusEarth * c
        if (isDoubleDistance) {
            d *= 2
        }
        return Math.round(d)
    }

    private fun degToRadius(deg: Float): Double {
        return deg * (Math.PI / 180)
    }

    fun getTravelledAroundTheWorldNumber(totalKms: Int): Float {
        return totalKms.toFloat() / WORLD_CIRCUMFERENCE
    }
}
