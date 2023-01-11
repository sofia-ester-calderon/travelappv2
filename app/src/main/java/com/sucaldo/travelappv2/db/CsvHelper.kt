package com.sucaldo.travelappv2.db

import android.os.Environment
import android.util.Log
import com.opencsv.CSVReader
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.util.DateFormat
import com.sucaldo.travelappv2.util.formatDate
import java.io.*
import java.util.*

class CsvHelper(private val myDB: DatabaseHelper) {

    private val downloadsDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    fun readCityLocationsCsvFile(inputStream: InputStream?): Boolean {
        try {
            val reader = CSVReader(InputStreamReader(inputStream))
            val csvLines = reader.readAll()
            for (nextLine in csvLines) {
                if (nextLine.size == 4) {
                    Log.d("CSV_CityLoc", "LOCATION: ${nextLine[0]}, ${nextLine[1]}")
                    myDB.addCityLocation(
                        nextLine[0],
                        nextLine[1], nextLine[2].toFloat(), nextLine[3].toFloat()
                    )
                }
            }
            return true
        } catch (e: IOException) {
            Log.e("CSV_CityLoc", "Line in .csv file could not be read")
            return false
        }
    }

    fun readTripsCsvFile(inputStream: InputStream?): Boolean {
        try {
            val reader = CSVReader(InputStreamReader(inputStream))
            val csvLines = reader.readAll()
            for (nextLine in csvLines) {
                if (nextLine.size == 11) {
                    val newTrip = Trip(
                        groupId = nextLine[0].toInt(),
                        fromCountry = nextLine[1],
                        fromCity = nextLine[2],
                        toCountry = nextLine[3],
                        toCity = nextLine[4],
                        description = nextLine[5],
                        startDate = nextLine[6],
                        endDate = nextLine[7],
                        distance = nextLine[8],
                        toContinent = nextLine[9],
                        type = nextLine[10],
                    )
                    Log.d("CSV_trips", "TRIP: ${nextLine[6]}, ${nextLine[4]}")
                    myDB.addTrip(newTrip)
                }
            }
            return true
        } catch (e: IOException) {
            Log.e("CSV_trips", "Line in .csv file could not be read")
            return false
        }
    }

    fun writeTripsToCsv() {
        val file = File(downloadsDir, "${formatDate(Date(), DateFormat.CSV)}_trips.csv")
        val csvString = StringBuilder()
        val trips: List<Trip> = myDB.allTrips
        for (trip in trips) {
            csvString.append(trip.groupId)
                .append(COLUMN_SEPARATOR)
                .append(trip.fromCountry)
                .append(COLUMN_SEPARATOR)
                .append(trip.fromCity)
                .append(COLUMN_SEPARATOR)
                .append(trip.toCountry)
                .append(COLUMN_SEPARATOR)
                .append(trip.toCity)
                .append(COLUMN_SEPARATOR)
                .append(trip.description)
                .append(COLUMN_SEPARATOR)
                .append(trip.startDate.toString())
                .append(COLUMN_SEPARATOR)
                .append(trip.endDate ?: "")
                .append(COLUMN_SEPARATOR)
                .append(trip.distance)
                .append(COLUMN_SEPARATOR)
                .append(trip.toContinent)
                .append(COLUMN_SEPARATOR)
                .append(trip.type)
                .append(ROW_SEPARATOR)
        }
        writeCsvFile(file, csvString.toString())
    }

    fun writeCityLocationsToCsv() {
        val file = File(downloadsDir, "${formatDate(Date(), DateFormat.CSV)}_city_locations.csv")
        val csvString = StringBuilder()
        val cityLocations: List<CityLocation> = myDB.allCityLocations
        for (cityLocation in cityLocations) {
            csvString.append(cityLocation.country)
                .append(COLUMN_SEPARATOR)
                .append(cityLocation.city)
                .append(COLUMN_SEPARATOR)
                .append(cityLocation.latitude)
                .append(COLUMN_SEPARATOR)
                .append(cityLocation.longitude)
                .append(ROW_SEPARATOR)
        }
        writeCsvFile(file, csvString.toString())
    }

    private fun writeCsvFile(file: File, content: String) {
        val fos = FileOutputStream(file)
        fos.write(content.toByteArray())
        fos.close()
    }

    companion object {
        private const val COLUMN_SEPARATOR = ","
        private const val ROW_SEPARATOR = "\n"
    }
}
