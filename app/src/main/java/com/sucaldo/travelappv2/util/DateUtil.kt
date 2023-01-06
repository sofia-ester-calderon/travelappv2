package com.sucaldo.travelappv2.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: Date, format: String = DateFormat.PRETTY): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
}

fun getDateFromString(stringDate: String, format: String): Date {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(stringDate)!!
    } catch (e: ParseException) {
        Date()
    }
}

class DateFormat {
    companion object {
        const val DB = "EEE MMM dd HH:mm:ss zzz yyyy"
        const val PRETTY = "dd.MM.yy"
        const val CSV = "yyyyMMdd"
    }
}
