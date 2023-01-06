package com.sucaldo.travelappv2.data

import android.database.Cursor
import com.sucaldo.travelappv2.util.DateFormat
import com.sucaldo.travelappv2.util.formatDate
import com.sucaldo.travelappv2.util.getDateFromString
import java.util.*

class Trip {
    var id: Int? = null
    var groupId: Int? = null
    var fromCountry: String
    var fromCity: String
    var toCountry: String
    var toCity: String
    var description: String
    var startDate: Date
    var endDate: Date? = null
    var distance: Long? = null
    var toContinent: String
    var type: TripType

    constructor(
        id: Int?,
        groupId: Int?,
        fromCountry: String,
        fromCity: String,
        toCountry: String,
        toCity: String,
        description: String,
        startDate: String,
        endDate: String,
        toContinent: String,
        type: TripType,
    ) {
        this.id = id
        this.groupId = groupId
        this.fromCountry = fromCountry
        this.fromCity = fromCity
        this.toCountry = toCountry
        this.toCity = toCity
        this.description = description
        this.startDate = getDateFromString(startDate, DateFormat.PRETTY)
        this.endDate = getDateFromString(endDate, DateFormat.PRETTY)
        this.type = type
        this.toContinent = toContinent
    }

    constructor(
        groupId: Int,
        fromCountry: String,
        fromCity: String,
        toCountry: String,
        toCity: String,
        description: String,
        startDate: String,
        endDate: String?,
        distance: String,
        toContinent: String,
        type: String,
    ) {
        this.groupId = groupId
        this.fromCountry = fromCountry
        this.fromCity = fromCity
        this.toCountry = toCountry
        this.toCity = toCity
        this.description = description
        this.distance = distance.toLong()
        this.toContinent = toContinent
        this.type = TripType.valueOf(type)
        this.startDate = getDateFromString(startDate, DateFormat.DB)
        this.endDate = if (endDate.isNullOrBlank()) null else getDateFromString(endDate, DateFormat.DB)
    }

    constructor(data: Cursor) {
        fromCountry = data.getString(1)
        fromCity = data.getString(2)
        toCountry = data.getString(3)
        toCity = data.getString(4)
        description = data.getString(5)
        id = data.getInt(0)
        groupId = data.getInt(8)
        distance = data.getLong(9)
        toContinent = data.getString(10)
        type = TripType.valueOf(data.getString(11))

        startDate = getDateFromString(data.getString(6), DateFormat.DB)
        if (data.getString(7) != null) {
            endDate = getDateFromString(data.getString(7), DateFormat.DB)
        }
    }

    fun getPickerFormattedStartDate(): String {
        return formatDate(startDate)
    }

    fun getPickerFormattedEndDate(): String {
        return if (endDate == null) {
            ""
        } else formatDate(endDate!!)
    }

}

enum class TripType {
    RETURN, MULTI_STOP, ONE_WAY, MULTI_STOP_LAST_STOP,
}