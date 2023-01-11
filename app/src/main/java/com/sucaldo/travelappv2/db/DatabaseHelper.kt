package com.sucaldo.travelappv2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.anychart.chart.common.dataentry.CategoryValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.sucaldo.travelappv2.data.CONTINENTS
import com.sucaldo.travelappv2.data.CityLocation
import com.sucaldo.travelappv2.data.Trip
import com.sucaldo.travelappv2.util.ChartHelper
import com.sucaldo.travelappv2.util.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // SQLite does not have data type varchar() or Date
        val createTableTrips =
            "CREATE TABLE " + TABLE_TRIPS + " (" + COL_TRIPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " " + COL_TRIPS_FROM_COUNTRY + " TEXT, " + COL_TRIPS_FROM_CITY + " TEXT, " + COL_TRIPS_TO_COUNTRY + " TEXT, " + COL_TRIPS_TO_CITY + " TEXT," +
                    " " + COL_TRIPS_DESCRIPTION + " TEXT, " + COL_TRIPS_START_DATE + " TEXT, " + COL_TRIPS_END_DATE + " TEXT, " + COL_TRIPS_GRP_ID + " INTEGER," +
                    " " + COL_TRIPS_DISTANCE + " INTEGER, " + COL_TRIPS_CONTINENT + " TEXT, " + COL_TRIPS_TYPE + " TEXT)"
        db.execSQL(createTableTrips)
        val createTableCityLoc =
            "CREATE TABLE " + TABLE_CITY_LOC + " (" + COL_CITY_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " " + COL_CITY_LOC_COUNTRY + " TEXT, " + COL_CITY_LOC_CITY + " TEXT, " + COL_CITY_LOC_LAT + " FLOAT, " +
                    " " + COL_CITY_LOC_LONG + " FLOAT)"
        db.execSQL(createTableCityLoc)
//        val createTableCountries = ("CREATE TABLE " + TABLE_COUNTRIES + " (" + COL_COUNTRIES_ID +
//                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_COUNTRIES_CONTINENT + " TEXT, " + COL_COUNTRIES_COUNTRY
//                + " TEXT) ")
//        db.execSQL(createTableCountries)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_TRIPS)
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_CITY_LOC)
//        db.execSQL("DROP IF TABLE EXISTS " + TABLE_COUNTRIES)
        onCreate(db)
    }

    /*
     ********* TABLE TRIPS **********************
     */
    val isTripTableEmpty: Boolean
        get() = isTableEmpty(TABLE_TRIPS)

    fun addTrip(trip: Trip): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TRIPS_FROM_COUNTRY, trip.fromCountry)
        contentValues.put(COL_TRIPS_FROM_CITY, trip.fromCity)
        contentValues.put(COL_TRIPS_TO_COUNTRY, trip.toCountry)
        contentValues.put(COL_TRIPS_TO_CITY, trip.toCity)
        contentValues.put(COL_TRIPS_DESCRIPTION, trip.description)
        contentValues.put(COL_TRIPS_START_DATE, trip.startDate.toString())
        contentValues.put(COL_TRIPS_TYPE, trip.type.toString())
        if (trip.endDate != null) {
            contentValues.put(COL_TRIPS_END_DATE, trip.endDate.toString())
        }
        contentValues.put(COL_TRIPS_DISTANCE, trip.distance)
        contentValues.put(COL_TRIPS_CONTINENT, trip.toContinent)
        if (trip.groupId == null) {
            contentValues.put(
                COL_TRIPS_GRP_ID,
                nextAvailableGroupId
            )
        } else {
            contentValues.put(COL_TRIPS_GRP_ID, trip.groupId)
        }

        // return = -1 if error
        val result = db.insert(TABLE_TRIPS, null, contentValues)
        return result != -1L
    }

    // Group Ids are used for differentiation of trips
    private val nextAvailableGroupId: Int
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                "SELECT IFNULL(MAX(" + COL_TRIPS_GRP_ID + " ),0) FROM " + TABLE_TRIPS,
                null
            )
            try {
                if (data.moveToNext()) {
                    var lastGroupId = data.getInt(0)
                    return ++lastGroupId
                }
                return 0
            } finally {
                closeCursor(data)
            }
        }

//    fun getAllTripsOfMultiStopSortedByDate(groupId: Int): List<Trip> {
//        val db = this.writableDatabase
//        val data = db.rawQuery(
//            "SELECT * FROM " + TABLE_TRIPS + " WHERE " + COL_TRIPS_GRP_ID +
//                    " = " + groupId, null
//        )
//        val numRows = data.count
//        if (numRows == 0) {
//            // empty list will be returned
//            return ArrayList<Trip>()
//        } else {
//            val trips: MutableList<Trip> = ArrayList<Trip>()
//            while (data.moveToNext()) {
//                trips.add(Trip(data))
//            }
//            Collections.sort(trips)
//            return trips
//        }
//    }

    val lastTripId: Int
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery("SELECT last_insert_rowid()", null)
            try {
                return if (data.moveToNext()) {
                    data.getInt(0)
                } else 0
            } finally {
                closeCursor(data)
            }
        }

    fun getTripById(id: Int): Trip? {
        val db = this.writableDatabase
        val data = db.rawQuery(
            ("SELECT * FROM " + TABLE_TRIPS +
                    " WHERE " + COL_TRIPS_ID + " = '" + id + "'"), null
        )
        return if (data.moveToNext()) {
            Trip(data)
        } else null
    }

    fun updateTrip(trip: Trip) {
        val db = this.writableDatabase
        db.execSQL(
            ("UPDATE " + TABLE_TRIPS + " SET " +
                    COL_TRIPS_FROM_COUNTRY + " = '" + trip.fromCountry + "'," +
                    COL_TRIPS_FROM_CITY + " = '" + trip.fromCity + "'," +
                    COL_TRIPS_TO_COUNTRY + " = '" + trip.toCountry + "'," +
                    COL_TRIPS_TO_CITY + " = '" + trip.toCity + "'," +
                    COL_TRIPS_DESCRIPTION + " = '" + trip.description + "'," +
                    COL_TRIPS_START_DATE + " = '" + trip.startDate + "'," +
                    COL_TRIPS_END_DATE + " = '" + trip.endDate + "'," +
                    COL_TRIPS_GRP_ID + " = '" + trip.groupId + "'," +
                    COL_TRIPS_DISTANCE + " = '" + trip.distance + "', " +
                    COL_TRIPS_CONTINENT + " = '" + trip.toContinent + "', " +
                    COL_TRIPS_TYPE + " = '" + trip.type + "' " +
                    " WHERE " + COL_TRIPS_ID + " = " + trip.id)
        )
    }

    fun deleteTrip(id: Int) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + TABLE_TRIPS + " WHERE " + COL_TRIPS_ID + " = " + id)
    }// empty list will be returned

    // SELECT DISTINCT(EXTRACT(YEAR FROM TIMESTAMP 'start date') not possible because date is stored as
    // a string due to SQLite restrictions
    val allYearsOfTrips: List<Int>
        get() {
            val db = this.writableDatabase
            // SELECT DISTINCT(EXTRACT(YEAR FROM TIMESTAMP 'start date') not possible because date is stored as
            // a string due to SQLite restrictions
            val data = db.rawQuery("SELECT " + COL_TRIPS_START_DATE + " FROM " + TABLE_TRIPS, null)
            try {
                val numRows = data.count
                if (numRows == 0) {
                    // empty list will be returned
                    return ArrayList()
                } else {
                    val years: MutableList<Int> = ArrayList()
                    while (data.moveToNext()) {
                        val dateString = data.getString(0)
                        var startDate: Date?
                        try {
                            startDate = SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(
                                dateString
                            )
                        } catch (e: ParseException) {
                            startDate = Date()
                        }
                        val cal = Calendar.getInstance()
                        cal.time = startDate!!
                        val year = cal[Calendar.YEAR]
                        if (!years.contains(year)) {
                            years.add(year)
                        }
                    }
                    Collections.sort(years)
                    return years
                }
            } finally {
                closeCursor(data)
            }
        }

    fun getTripsOfYearSortedByDate(year: Int): List<Trip> {
        val db = this.writableDatabase
        val data = db.rawQuery(
            ("SELECT * FROM " + TABLE_TRIPS + " WHERE " + COL_TRIPS_START_DATE +
                    " LIKE '%" + year + "' "), null
        )
        val numRows = data.count
        if (numRows == 0) {
            // empty list will be returned
            return listOf()
        } else {
            val trips: MutableList<Trip> = ArrayList<Trip>()
            while (data.moveToNext()) {
                trips.add(Trip(data))
            }
            trips.sortBy { it.startDate }
            return trips
        }
    }

    fun getTripsThatContainSpecificLocation(country: String, city: String): List<Trip> {
        val db = this.writableDatabase
        val data = db.rawQuery(
            ("SELECT * FROM " + TABLE_TRIPS +
                    " WHERE (" + COL_TRIPS_FROM_COUNTRY + " = '" + country + "' AND " + COL_TRIPS_FROM_CITY + " " +
                    " = '" + city + "') OR (" + COL_TRIPS_TO_COUNTRY + " = '" + country + "' AND " + COL_TRIPS_TO_CITY +
                    " = '" + city + "')"), null
        )
        val numRows = data.count
        try {
            if (numRows == 0) {
                return ArrayList<Trip>()
            } else {
                val trips: MutableList<Trip> = ArrayList<Trip>()
                while (data.moveToNext()) {
                    trips.add(Trip(data))
                }
                return trips
            }
        } finally {
            closeCursor(data)
        }
    }

    val allTrips: List<Trip>
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery("SELECT * FROM " + TABLE_TRIPS, null)
            val numRows = data.count
            if (numRows == 0) {
                return ArrayList()
            } else {
                val trips: MutableList<Trip> = ArrayList<Trip>()
                while (data.moveToNext()) {
                    trips.add(Trip(data))
                }
                return trips
            }
        }

    /*
       ********* TABLE CITY_LOC  **********************
       */
    fun addCityLocation(country: String?, city: String?, latitude: Float?, longitude: Float?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_CITY_LOC_COUNTRY, country)
        contentValues.put(COL_CITY_LOC_CITY, city)
        contentValues.put(COL_CITY_LOC_LAT, latitude)
        contentValues.put(COL_CITY_LOC_LONG, longitude)
        db.insert(TABLE_CITY_LOC, null, contentValues)
    }

    val isCityLocTableEmpty: Boolean
        get() = isTableEmpty(TABLE_CITY_LOC)

    fun getLocationOfCity(country: String, city: String): CityLocation? {
        if (country.isBlank() || city.isBlank()) return null
        val cityLocations: List<CityLocation> = getStoredCityCoordinates(country, city)
        return if (cityLocations.isEmpty()) {
            null
        } else cityLocations[0]
    }

    fun saveCityLocationIfNotInDb(cityLocation: CityLocation) {
        if (isCityLocationInDb(cityLocation)) {
            return
        }
        addCityLocation(
            cityLocation.country,
            cityLocation.city,
            cityLocation.latitude,
            cityLocation.longitude
        )
    }

    private fun isCityLocationInDb(cityLocation: CityLocation): Boolean {
        val db = this.writableDatabase
        val data = db.rawQuery(
            ("SELECT COUNT(*) FROM " + TABLE_CITY_LOC + " WHERE " + COL_CITY_LOC_COUNTRY +
                    " = '" + cityLocation.country + "' AND " + COL_CITY_LOC_CITY + " = '" + cityLocation.city + "'"),
            null
        )
        try {
            if (data.moveToNext()) {
                val rowCount = data.getInt(0)
                return rowCount >= 1
            }
            return false
        } finally {
            closeCursor(data)
        }
    }

    fun getStoredCityCoordinates(country: String, city: String): List<CityLocation> {
        val db = this.writableDatabase
        val cityQuery = if ((city == "")) "%" else city
        val countryQuery = if ((country == "")) "%" else country
        val data = db.rawQuery(
            ("SELECT " + COL_CITY_LOC_CITY + ", " + COL_CITY_LOC_COUNTRY +
                    ", " + COL_CITY_LOC_LAT + ", " + COL_CITY_LOC_LONG + ", " + COL_CITY_LOC_ID +
                    " FROM " + TABLE_CITY_LOC + " WHERE " + COL_CITY_LOC_COUNTRY + " LIKE '" + countryQuery + "' " +
                    " AND " + COL_CITY_LOC_CITY + " LIKE '" + cityQuery + "'"), null
        )
        val numRows = data.count
        if (numRows == 0) {
            return ArrayList<CityLocation>()
        } else {
            val cityCoordinates: MutableList<CityLocation> = ArrayList<CityLocation>()
            while (data.moveToNext()) {
                cityCoordinates.add(CityLocation(data))
            }
            return cityCoordinates
        }
    }

    fun updateCityLocation(cityLocation: CityLocation) {
        val db = this.writableDatabase
        db.execSQL(
            ("UPDATE " + TABLE_CITY_LOC + " SET " +
                    COL_CITY_LOC_COUNTRY + " = '" + cityLocation.country + "'," +
                    COL_CITY_LOC_CITY + " = '" + cityLocation.city + "'," +
                    COL_CITY_LOC_LAT + " = " + cityLocation.latitude + "," +
                    COL_CITY_LOC_LONG + " = " + cityLocation.longitude + " " +
                    " WHERE " + COL_CITY_LOC_ID + " = " + cityLocation.id)
        )
    }

    val allCityLocations: List<CityLocation>
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT " + COL_CITY_LOC_CITY + ", " + COL_CITY_LOC_COUNTRY +
                        ", " + COL_CITY_LOC_LAT + ", " + COL_CITY_LOC_LONG + ", " + COL_CITY_LOC_ID +
                        " FROM " + TABLE_CITY_LOC), null
            )
            val numRows = data.count
            if (numRows == 0) {
                return ArrayList()
            } else {
                val cityLocations: MutableList<CityLocation> = ArrayList<CityLocation>()
                while (data.moveToNext()) {
                    cityLocations.add(CityLocation(data))
                }
                return cityLocations
            }
        }

    /*
       ********* TABLE COUNTRIES **********************
       */
//    fun addCountryContinentItem(continent: String?, country: String?) {
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COL_COUNTRIES_CONTINENT, continent)
//        contentValues.put(COL_COUNTRIES_COUNTRY, country)
//        db.insert(TABLE_COUNTRIES, null, contentValues)
//    }
//
//    fun getContinentOfCountry(country: String): String? {
//        val db = this.writableDatabase
//        val data = db.rawQuery(
//            ("SELECT " + COL_COUNTRIES_CONTINENT + " FROM " + TABLE_COUNTRIES +
//                    " WHERE " + COL_COUNTRIES_COUNTRY + " = '" + country + "'"), null
//        )
//        try {
//            return if (data.moveToNext()) {
//                data.getString(0)
//            } else null
//        } finally {
//            closeCursor(data)
//        }
//    }
//
//    val isCountriesTableEmpty: Boolean
//        get() = isTableEmpty(TABLE_COUNTRIES)
//
//    // empty list will be returned
//    val countries: List<String>
//        get() {
//            val db = this.writableDatabase
//            val data = db.rawQuery(
//                "SELECT DISTINCT " + COL_COUNTRIES_COUNTRY + " FROM " + TABLE_COUNTRIES,
//                null
//            )
//            val numRows = data.count
//            try {
//                if (numRows == 0) {
//                    // empty list will be returned
//                    return ArrayList()
//                } else {
//                    val countries: MutableList<String> = ArrayList()
//                    while (data.moveToNext()) {
//                        countries.add(data.getString(0))
//                    }
//                    Collections.sort(countries)
//                    return countries
//                }
//            } finally {
//                closeCursor(data)
//            }
//        }
//
//    val allCountriesContinents: List<Any>
//        get() {
//            val db = this.writableDatabase
//            val data = db.rawQuery("SELECT * FROM " + TABLE_COUNTRIES, null)
//            val numRows = data.count
//            if (numRows == 0) {
//                return ArrayList()
//            } else {
//                val countriesContinents: MutableList<CountriesContinents> =
//                    ArrayList<CountriesContinents>()
//                while (data.moveToNext()) {
//                    countriesContinents.add(CountriesContinents(data))
//                }
//                return countriesContinents
//            }
//        }

    /*
       ********* STATISTICS / CHARTS **********************
       */
    fun getTop10VisitedPlaces(years: List<String> = listOf()): List<DataEntry> {
        val db = this.writableDatabase
        val yearQuery = buildYearQuery(years)
        val data = db.rawQuery(
            ("SELECT " + COL_TRIPS_TO_CITY + ", COUNT(" + COL_TRIPS_TO_CITY + ") AS total" +
                    " FROM (" +
                    " SELECT " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY +
                    " FROM " + TABLE_TRIPS +
                    " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')" +
                    yearQuery +
                    " GROUP BY " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY +
                    " ) AS cities" +
                    " GROUP BY " + COL_TRIPS_TO_CITY +
                    " ORDER BY total DESC" +
                    " LIMIT 10"), null
        )
        val numRows = data.count
        try {
            if (numRows == 0) {
                return ArrayList()
            } else {
                val top10Places: MutableList<DataEntry> = ArrayList()
                while (data.moveToNext()) {
                    top10Places.add(ValueDataEntry(data.getString(0), data.getInt(1)))
                }
                return top10Places
            }
        } finally {
            closeCursor(data)
        }
    }

    private fun buildYearQuery(years: List<String>): String {
        if (years.isEmpty()) {
            return ""
        }
        val query = StringBuilder(" AND ((" + COL_TRIPS_START_DATE + " LIKE '%" + years[0] + "')")
        for (i in 1 until years.size) {
            query.append(" OR (" + COL_TRIPS_START_DATE + " LIKE '%").append(
                years[i]
            ).append("')")
        }
        query.append(")")
        return query.toString()
    }

    val numberOfVisitedPlaces: Int
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT COUNT(DISTINCT(" + COL_TRIPS_TO_CITY + "))" +
                        " FROM " + TABLE_TRIPS +
                        " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')"), null
            )
            try {
                return if (data.moveToNext()) {
                    data.getInt(0)
                } else -1
            } finally {
                closeCursor(data)
            }
        }

        val visitedCountries: List<DataEntry>
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT + ", COUNT(" + COL_TRIPS_TO_COUNTRY + ")" +
                        " FROM (" +
                        " SELECT " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT +
                        " FROM " + TABLE_TRIPS +
                        " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')" +
                        " GROUP BY " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT +
                        " ) AS countries" +
                        " GROUP BY " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT +
                        " ORDER BY " + COL_TRIPS_CONTINENT), null
            )
            val numRows = data.count
            try {
                if (numRows == 0) {
                    return ArrayList()
                } else {
                    val visitedCountries: MutableList<DataEntry> = ArrayList()
                    while (data.moveToNext()) {
                        visitedCountries.add(
                            ChartHelper.CustomCategoryValueDataEntry(
                                data.getString(0), data.getString(1), data.getInt(2)
                            )
                        )
                    }
                    for (dataEntry: DataEntry in visitedCountries) {
                        val customEntry: ChartHelper.CustomCategoryValueDataEntry =
                            dataEntry as ChartHelper.CustomCategoryValueDataEntry
                        val visitedCountry = customEntry.getValue("x") as String
                        val data2 = db.rawQuery(
                            ("SELECT * FROM " + TABLE_TRIPS + " WHERE "
                                    + COL_TRIPS_TO_COUNTRY + " = '" + visitedCountry + "' " +
                                    " AND " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')"),
                            null
                        )
                        val trips: MutableList<Trip> = ArrayList<Trip>()
                        while (data2.moveToNext()) {
                            trips.add(Trip(data2))
                        }
                        customEntry.setTripsInfo(trips)
                        closeCursor(data2)
                    }
                    return visitedCountries
                }
            } finally {
                closeCursor(data)
            }
        }

    val numberOfVisitedCountries: Int
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT COUNT(DISTINCT(" + COL_TRIPS_TO_COUNTRY + "))" +
                        " FROM " + TABLE_TRIPS +
                        " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')"), null
            )
            try {
                return if (data.moveToNext()) {
                    data.getInt(0)
                } else -1
            } finally {
                closeCursor(data)
            }
        }
    val visitedPlaces: List<DataEntry>
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT + ", COUNT(" + COL_TRIPS_TO_CITY + ")" +
                        " FROM (" +
                        " SELECT " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT +
                        " FROM " + TABLE_TRIPS +
                        " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')" +
                        " GROUP BY " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT +
                        " ) AS cities" +
                        " GROUP BY " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT +
                        " ORDER BY " + COL_TRIPS_CONTINENT), null
            )
            val numRows = data.count
            try {
                if (numRows == 0) {
                    return ArrayList()
                } else {
                    val visitedPlaces: MutableList<DataEntry> = ArrayList()
                    while (data.moveToNext()) {
                        visitedPlaces.add(
                            CategoryValueDataEntry(
                                data.getString(0), data.getString(1), data.getInt(2)
                            )
                        )
                    }
                    return visitedPlaces
                }
            } finally {
                closeCursor(data)
            }
        }
    val kmsPerContinentPerYear: List<DataEntry>
        get() {
            val allYears = allYearsOfTrips
            val areaChartList: MutableList<DataEntry> = ArrayList()
            for (year: Int in allYears) {
                val db = this.writableDatabase
                val data = db.rawQuery(
                    ("SELECT " + COL_TRIPS_CONTINENT + ", SUM(" + COL_TRIPS_DISTANCE + ")" +
                            " FROM " + TABLE_TRIPS +
                            " WHERE " + COL_TRIPS_START_DATE + " LIKE '%" + year + "'" +
                            " GROUP BY " + COL_TRIPS_CONTINENT), null
                )
                val numRows = data.count
                try {
                    if (numRows == 0) {
                        continue
                    }
                    val continentsAndKmsMap: MutableMap<String, Int> = HashMap()
                    while (data.moveToNext()) {
                        continentsAndKmsMap[data.getString(0)] = data.getInt(1)
                    }
                    areaChartList.add(
                        ChartHelper.CustomDataEntry(
                            year.toString(),
                            getKmsPerContinentList(continentsAndKmsMap)
                        )
                    )
                } finally {
                    closeCursor(data)
                }
            }
            return areaChartList
        }

    private fun getKmsPerContinentList(continentsAndKmsMap: Map<String, Int>): List<Int?> {
        val kmsPerContinentList: MutableList<Int?> = ArrayList()
        for (continent: String in CONTINENTS) {
            if (continentsAndKmsMap.containsKey(continent)) {
                kmsPerContinentList.add(continentsAndKmsMap[continent])
            } else {
                kmsPerContinentList.add(0)
            }
        }
        return kmsPerContinentList
    }

        val kmsAndTripsPerYear: List<DataEntry>
        get() {
            val allYears = allYearsOfTrips
            val bubbleChartList: MutableList<DataEntry> = ArrayList()
            for (year: Int in allYears) {
                val db = this.writableDatabase
                val data = db.rawQuery(
                    ("SELECT SUM(" + COL_TRIPS_DISTANCE + "), COUNT(DISTINCT(" + COL_TRIPS_GRP_ID + "))" +
                            " FROM " + TABLE_TRIPS +
                            " WHERE " + COL_TRIPS_START_DATE + " LIKE '%" + year + "'"), null
                )
                val data2 = db.rawQuery(
                    ("SELECT DISTINCT(" + COL_TRIPS_TO_COUNTRY + ")" +
                            " FROM " + TABLE_TRIPS +
                            " WHERE " + COL_TRIPS_START_DATE + " LIKE '%" + year + "'"), null
                )
                val numRows2 = data2.count
                val countries: MutableList<String> = ArrayList()
                try {
                    if (numRows2 == 0) {
                        continue
                    }
                    while (data2.moveToNext()) {
                        countries.add(data2.getString(0))
                    }
                } finally {
                    closeCursor(data2)
                }
                val numRows = data.count
                try {
                    if (numRows == 0) {
                        continue
                    }
                    while (data.moveToNext()) {
                        bubbleChartList.add(
                            ChartHelper.CustomBubbleDataEntry(
                                year,
                                data.getInt(1),
                                data.getInt(0),
                                countries
                            )
                        )
                    }
                } finally {
                    closeCursor(data)
                }
            }
            return bubbleChartList
        }

    val totalTravelledKms: Int
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT SUM(" + COL_TRIPS_DISTANCE + ")" +
                        " FROM " + TABLE_TRIPS), null
            )
            try {
                return if (data.moveToNext()) {
                    data.getInt(0)
                } else -1
            } finally {
                closeCursor(data)
            }
        }
    val latitudeAndLongitudeOfVisitedCities: List<CityLocation>
        get() {
            val db = this.writableDatabase
            val data = db.rawQuery(
                ("SELECT DISTINCT(" + TABLE_TRIPS + "." + COL_TRIPS_TO_CITY + ")" +
                        ", " + TABLE_TRIPS + "." + COL_TRIPS_TO_COUNTRY +
                        ", " + TABLE_CITY_LOC + "." + COL_CITY_LOC_LAT +
                        ", " + TABLE_CITY_LOC + "." + COL_CITY_LOC_LONG +
                        ", " + TABLE_CITY_LOC + "." + COL_CITY_LOC_ID +
                        " FROM " + TABLE_TRIPS +
                        " INNER JOIN " + TABLE_CITY_LOC +
                        " ON " + TABLE_TRIPS + "." + COL_TRIPS_TO_CITY + " = " + TABLE_CITY_LOC + "." + COL_CITY_LOC_CITY +
                        " AND " + TABLE_TRIPS + "." + COL_TRIPS_TO_COUNTRY + " = " + TABLE_CITY_LOC + "." + COL_CITY_LOC_COUNTRY +
                        " ORDER BY " + TABLE_TRIPS + "." + COL_TRIPS_TO_CITY), null
            )
            val numRows = data.count
            if (numRows == 0) {
                return ArrayList()
            }
            val latAndLongOfVisitedCities: MutableList<CityLocation> = ArrayList<CityLocation>()
            while (data.moveToNext()) {
                latAndLongOfVisitedCities.add(CityLocation(data))
            }
            return latAndLongOfVisitedCities
        }

    /*
       ********* GENERAL **********************
       */
    private fun isTableEmpty(table: String): Boolean {
        val db = this.writableDatabase
        val data = db.rawQuery("SELECT COUNT(*) FROM $table", null)
        try {
            if (data.moveToNext()) {
                val rowCount = data.getInt(0)
                // to consider all possible return values of count (0, -1, etc.)
                return rowCount < 1
            }
            return true
        } finally {
            closeCursor(data)
        }
    }

    private fun closeCursor(cursor: Cursor?) {
        cursor?.close()
    }

    companion object {
        private val DATABASE_NAME = "my_trips.db"
        private val TABLE_TRIPS = "trips"
        private val COL_TRIPS_ID = "ID"
        private val COL_TRIPS_FROM_COUNTRY = "FROMCOUNTRY"
        private val COL_TRIPS_FROM_CITY = "FROMCITY"
        private val COL_TRIPS_TO_COUNTRY = "TOCOUNTRY"
        private val COL_TRIPS_TO_CITY = "TOCITY"
        private val COL_TRIPS_DESCRIPTION = "DESCRIPTION"
        private val COL_TRIPS_START_DATE = "STARTDATE"
        private val COL_TRIPS_END_DATE = "ENDDATE"
        private val COL_TRIPS_GRP_ID = "GROUPID"
        private val COL_TRIPS_DISTANCE = "DISTANCE"
        private val COL_TRIPS_CONTINENT = "CONTINENT"
        private val COL_TRIPS_TYPE = "TYPE"
        private val TABLE_CITY_LOC = "city_loc"
        private val COL_CITY_LOC_ID = "ID"
        private val COL_CITY_LOC_COUNTRY = "COUNTRY"
        private val COL_CITY_LOC_CITY = "CITY"
        private val COL_CITY_LOC_LAT = "LATITUDE"
        private val COL_CITY_LOC_LONG = "LONGITUDE"
//        private val TABLE_COUNTRIES = "countries"
//        private val COL_COUNTRIES_ID = "ID"
//        private val COL_COUNTRIES_CONTINENT = "CONTINENT"
//        private val COL_COUNTRIES_COUNTRY = "COUNTRY"
//        val CONTINENTS = Arrays.asList(
//            "North America", "Middle East", "South America", "Africa", "Asia",
//            "Central America & Caribbean", "Oceania", "Europe"
//        )
    }
}