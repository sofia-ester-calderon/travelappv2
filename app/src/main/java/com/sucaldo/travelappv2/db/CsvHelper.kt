package com.sucaldo.travelappv2.db

import android.util.Log
import com.opencsv.CSVReader
import java.io.*

class CsvHelper (private val myDB: DatabaseHelper2) {

    fun readCityLocationsCsvFile(inputStream: InputStream?) {
        try {
            val reader = CSVReader(InputStreamReader(inputStream))
            var i = 0
//            var progressPercentage = 0
            val csvLines = reader.readAll()
            val IMPORT_TOTAL_STEPS = 40
            val importStep = csvLines.size / IMPORT_TOTAL_STEPS
            for (nextLine in csvLines) {
                // % is equivalent to MOD in Excel
//                if (i++ % importStep == 0) {
//                    progressPercentage += 100 / IMPORT_TOTAL_STEPS
//                    progressBar.progress = progressPercentage
//                }
                if (nextLine.size == 4) {
                    myDB.addCityLocation(
                        nextLine[0],
                        nextLine[1], nextLine[2].toFloat(), nextLine[3].toFloat()
                    )
                }
            }
//            progressBar.progress = 100
        } catch (e: IOException) {
            Log.e("CSV_CityLoc", "Line in .csv file could not be read")
        }
    }

    fun readTripsCsvFile(inputStream: InputStream?) {
//        try {
//            val reader = CSVReader(InputStreamReader(inputStream))
//            var nextLine: Array<String>
//            while (reader.readNext().also { nextLine = it } != null) {
//                if (nextLine.size == 11) {
//                    Log.d("CSV_trips", nextLine[3])
//                    val newTrip = Trip(
//                        nextLine[0].toInt(),
//                        nextLine[1],
//                        nextLine[2],
//                        nextLine[3],
//                        nextLine[4],
//                        nextLine[5],
//                        nextLine[6],
//                        nextLine[7],
//                        nextLine[8].toInt(),
//                        nextLine[9],
//                        TripType.valueOf(nextLine[10])
//                    )
//                    myDB.addTrip(newTrip)
//                }
//            }
//        } catch (e: IOException) {
//            Log.e("CSV_trips", "Line in .csv file could not be read")
//        }
    }

    fun writeTripsToCsv(rootFile: File?) {
//        val csvFile = File(rootFile, "trips.csv")
//        val csvString = StringBuilder()
//        val trips: List<Trip> = myDB.getAllTrips()
//        for (trip in trips) {
//            csvString.append(trip.getGroupId())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getFromCountry())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getFromCity())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getToCountry())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getToCity())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getDescription())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getStartDate())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getEndDateAsString())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getDistance())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getToContinent())
//                .append(COLUMN_SEPARATOR)
//                .append(trip.getType())
//                .append(ROW_SEPARATOR)
//        }
//        writeCsvFile(csvFile, csvString.toString())
    }

    fun writeCityLocationsToCsv(rootFile: File?) {
//        val csvFile = File(rootFile, "city_locations.csv")
//        val csvString = StringBuilder()
//        val cityLocations: List<CityLocation> = myDB.allCityLocations
//        for (cityLocation in cityLocations) {
//            csvString.append(cityLocation.getCountry())
//                .append(COLUMN_SEPARATOR)
//                .append(cityLocation.getCity())
//                .append(COLUMN_SEPARATOR)
//                .append(cityLocation.getLatitude())
//                .append(COLUMN_SEPARATOR)
//                .append(cityLocation.getLongitude())
//                .append(ROW_SEPARATOR)
//        }
//        writeCsvFile(csvFile, csvString.toString())
    }

    fun writeCountriesContinentsToCsv(rootFile: File?) {
//        val csvFile = File(rootFile, "countries_continents.csv")
//        val csvString = StringBuilder()
//        val countriesContinents: List<CountriesContinents> = myDB.getAllCountriesContinents()
//        for (countriesContinent in countriesContinents) {
//            csvString.append(countriesContinent.getContinent())
//                .append(COLUMN_SEPARATOR)
//                .append(countriesContinent.getCountry())
//                .append(ROW_SEPARATOR)
//        }
//        writeCsvFile(csvFile, csvString.toString())
    }

    private fun writeCsvFile(file: File, content: String) {
        try {
            val fos = FileOutputStream(file)
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: IOException) {
            // TODO error handling
            e.printStackTrace()
        }
    }
    companion object {
        private const val COLUMN_SEPARATOR = ","
        private const val ROW_SEPARATOR = "\n"
    }
}
