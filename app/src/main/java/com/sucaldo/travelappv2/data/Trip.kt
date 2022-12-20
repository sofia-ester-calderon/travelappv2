package com.sucaldo.travelappv2.data

import android.database.Cursor
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Trip {
    var id: Int
    var groupId: Int
    var fromCountry: String
    var fromCity: String
    var toCountry: String
    var toCity: String
    var description: String
    var startDate: Date? = null
    var endDate: Date? = null
    var distance: Long
    var toContinent: String
    var type: TripType

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
                SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(data.getString(6))
        } catch (e: ParseException) {
        }
        if (data.getString(7) != null) {
            try {
                endDate =
                    SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(data.getString(7))
            } catch (e: ParseException) {
            }
        }
    }

    fun getPickerFormattedStartDate(): String {
        return formatDate(startDate!!, DateFormat.PICKER)
    }

    fun getPickerFormattedEndDate(): String {
        return if (endDate == null) {
            ""
        } else formatDate(endDate!!, DateFormat.PICKER)
    }

    private fun formatDate(date: Date, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }
}

enum class TripType {
    RETURN, ONE_WAY, MULTI
}