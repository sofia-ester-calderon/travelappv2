package com.sucaldo.travelappv2.data

import android.database.Cursor
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class Trip {
    var id: Int? = null
    var groupId: Int
    var fromCountry: String
    var fromCity: String
    var toCountry: String
    var toCity: String
    var description: String
    lateinit var startDate: Date
    var endDate: Date? = null
    var distance: Long
    var toContinent: String
    var type: TripType

    constructor(
        fromCountry: String,
        fromCity: String,
        toCountry: String,
        toCity: String,
        description: String,
        startDate: Date,
        endDate: Date,
        distance: Long,
        toContinent: String,
        type: TripType,
        groupId: Int,
    ) {
        this.groupId = groupId
        this.fromCountry = fromCountry
        this.fromCity = fromCity
        this.toCountry = toCountry
        this.toCity = toCity
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
        this.distance = distance
        this.toContinent = toContinent
        this.type = type
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

        try {
            startDate =
                SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(data.getString(6))!!
        } catch (e: ParseException) {
            LocalDate.now()
        }
        if (data.getString(7) != null) {
            try {
                endDate =
                    SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(data.getString(7))
            } catch (e: ParseException) {
                LocalDate.now()
            }
        }
    }

    fun getPickerFormattedStartDate(): String {
        return formatDate(startDate, DateFormat.PRETTY)
    }

    fun getPickerFormattedEndDate(): String {
        return if (endDate == null) {
            ""
        } else formatDate(endDate!!, DateFormat.PRETTY)
    }

    private fun formatDate(date: Date, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }
}

enum class TripType {
    RETURN, ONE_WAY, MULTI
}