package com.sucaldo.travelappv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.sucaldo.travelappv2.data.CityLocation;
import com.sucaldo.travelappv2.data.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_trips.db";

    private static final String TABLE_TRIPS = "trips";
    private static final String COL_TRIPS_ID = "ID";
    private static final String COL_TRIPS_FROM_COUNTRY = "FROMCOUNTRY";
    private static final String COL_TRIPS_FROM_CITY = "FROMCITY";
    private static final String COL_TRIPS_TO_COUNTRY = "TOCOUNTRY";
    private static final String COL_TRIPS_TO_CITY = "TOCITY";
    private static final String COL_TRIPS_DESCRIPTION = "DESCRIPTION";
    private static final String COL_TRIPS_START_DATE = "STARTDATE";
    private static final String COL_TRIPS_END_DATE = "ENDDATE";
    private static final String COL_TRIPS_GRP_ID = "GROUPID";
    private static final String COL_TRIPS_DISTANCE = "DISTANCE";
    private static final String COL_TRIPS_CONTINENT = "CONTINENT";
    private static final String COL_TRIPS_TYPE = "TYPE";

    private static final String TABLE_CITY_LOC = "city_loc";
    private static final String COL_CITY_LOC_ID = "ID";
    private static final String COL_CITY_LOC_COUNTRY = "COUNTRY";
    private static final String COL_CITY_LOC_CITY = "CITY";
    private static final String COL_CITY_LOC_LAT = "LATITUDE";
    private static final String COL_CITY_LOC_LONG = "LONGITUDE";

    private static final String TABLE_COUNTRIES = "countries";
    private static final String COL_COUNTRIES_ID = "ID";
    private static final String COL_COUNTRIES_CONTINENT = "CONTINENT";
    private static final String COL_COUNTRIES_COUNTRY = "COUNTRY";

    public static final List<String> CONTINENTS = Arrays.asList(
            "North America", "Middle East", "South America", "Africa", "Asia",
            "Central America & Caribbean", "Oceania", "Europe");

    // Initialization of Database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLite does not have data type varchar() or Date
        String createTableTrips = "CREATE TABLE " + TABLE_TRIPS + " (" + COL_TRIPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " " + COL_TRIPS_FROM_COUNTRY + " TEXT, " + COL_TRIPS_FROM_CITY + " TEXT, " + COL_TRIPS_TO_COUNTRY + " TEXT, " + COL_TRIPS_TO_CITY + " TEXT," +
                " " + COL_TRIPS_DESCRIPTION + " TEXT, " + COL_TRIPS_START_DATE + " TEXT, " + COL_TRIPS_END_DATE + " TEXT, " + COL_TRIPS_GRP_ID + " INTEGER," +
                " " + COL_TRIPS_DISTANCE + " INTEGER, " + COL_TRIPS_CONTINENT + " TEXT, " + COL_TRIPS_TYPE + " TEXT)";
        db.execSQL(createTableTrips);

        String createTableCityLoc = "CREATE TABLE " + TABLE_CITY_LOC + " (" + COL_CITY_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " " + COL_CITY_LOC_COUNTRY + " TEXT, " + COL_CITY_LOC_CITY + " TEXT, " + COL_CITY_LOC_LAT + " FLOAT, " +
                " " + COL_CITY_LOC_LONG + " FLOAT)";
        db.execSQL(createTableCityLoc);

        String createTableCountries = "CREATE TABLE " + TABLE_COUNTRIES + " (" + COL_COUNTRIES_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_COUNTRIES_CONTINENT + " TEXT, " + COL_COUNTRIES_COUNTRY
                + " TEXT) ";
        db.execSQL(createTableCountries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_CITY_LOC);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_COUNTRIES);
        onCreate(db);
    }

    /*
     ********* TABLE TRIPS **********************
     */

    public Boolean addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TRIPS_FROM_COUNTRY, trip.getFromCountry());
        contentValues.put(COL_TRIPS_FROM_CITY, trip.getFromCity());
        contentValues.put(COL_TRIPS_TO_COUNTRY, trip.getToCountry());
        contentValues.put(COL_TRIPS_TO_CITY, trip.getToCity());
        contentValues.put(COL_TRIPS_DESCRIPTION, trip.getDescription());
        contentValues.put(COL_TRIPS_START_DATE, trip.getStartDate().toString());
        contentValues.put(COL_TRIPS_TYPE, trip.getType().toString());
        if (trip.getEndDate() != null) {
            contentValues.put(COL_TRIPS_END_DATE, trip.getEndDate().toString());
        }
        contentValues.put(COL_TRIPS_DISTANCE, trip.getDistance());
        contentValues.put(COL_TRIPS_CONTINENT, trip.getToContinent());
        if (trip.getGroupId() == null) {
            contentValues.put(COL_TRIPS_GRP_ID, getNextAvailableGroupId());
        } else {
            contentValues.put(COL_TRIPS_GRP_ID, trip.getGroupId());
        }

        // return = -1 if error
        long result = db.insert(TABLE_TRIPS, null, contentValues);
        return result != -1;
    }

    // Group Ids are used for differentiation of trips
    private int getNextAvailableGroupId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT IFNULL(MAX(" + COL_TRIPS_GRP_ID + " ),0) FROM " + TABLE_TRIPS, null);
        try {
            if (data.moveToNext()) {
                int lastGroupId = data.getInt(0);
                return ++lastGroupId;
            }
            return 0;
        } finally {
            closeCursor(data);
        }
    }

//    public List<Trip> getAllTripsOfMultiStopSortedByDate(int groupId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_TRIPS + " WHERE " + COL_TRIPS_GRP_ID +
//                " = " + groupId, null);
//
//        int numRows = data.getCount();
//        if (numRows == 0) {
//            // empty list will be returned
//            return new ArrayList<>();
//        } else {
//            List<Trip> trips = new ArrayList<>();
//            while (data.moveToNext()) {
//                trips.add(new Trip(data));
//            }
//            Collections.sort(trips);
//            return trips;
//        }
//    }

    public int getLastTripId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT last_insert_rowid()", null);
        try {
            if (data.moveToNext()) {
                return data.getInt(0);
            }
            return 0;
        } finally {
            closeCursor(data);
        }
    }

    public Trip getTripById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_TRIPS +
                " WHERE " + COL_TRIPS_ID + " = '" + id + "'", null);

        if (data.moveToNext()) {
            return new Trip(data);
        }
        return null;
    }

    public void updateTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_TRIPS + " SET " +
                COL_TRIPS_FROM_COUNTRY + " = '" + trip.getFromCountry() + "'," +
                COL_TRIPS_FROM_CITY + " = '" + trip.getFromCity() + "'," +
                COL_TRIPS_TO_COUNTRY + " = '" + trip.getToCountry() + "'," +
                COL_TRIPS_TO_CITY + " = '" + trip.getToCity() + "'," +
                COL_TRIPS_DESCRIPTION + " = '" + trip.getDescription() + "'," +
                COL_TRIPS_START_DATE + " = '" + trip.getStartDate() + "'," +
                COL_TRIPS_END_DATE + " = '" + trip.getEndDate() + "'," +
                COL_TRIPS_GRP_ID + " = '" + trip.getGroupId() + "'," +
                COL_TRIPS_DISTANCE + " = '" + trip.getDistance() + "', " +
                COL_TRIPS_CONTINENT + " = '" + trip.getToContinent() + "', " +
                COL_TRIPS_TYPE + " = '" + trip.getType() + "' " +
                " WHERE " + COL_TRIPS_ID + " = " + trip.getId());
    }

    public void deleteTrip(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRIPS + " WHERE " + COL_TRIPS_ID + " = " + id);
    }

//    public List<Integer> getAllYearsOfTrips() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        // SELECT DISTINCT(EXTRACT(YEAR FROM TIMESTAMP 'start date') not possible because date is stored as
//        // a string due to SQLite restrictions
//        Cursor data = db.rawQuery("SELECT " + COL_TRIPS_START_DATE + " FROM " + TABLE_TRIPS, null);
//        try {
//            int numRows = data.getCount();
//            if (numRows == 0) {
//                // empty list will be returned
//                return new ArrayList<>();
//            } else {
//                List<Integer> years = new ArrayList<>();
//                while (data.moveToNext()) {
//                    String dateString = data.getString(0);
//                    Date startDate;
//                    try {
//                        startDate = new SimpleDateFormat(DateFormat.DB, Locale.getDefault()).parse(dateString);
//                    } catch (ParseException e) {
//                        startDate = new Date();
//                    }
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(startDate);
//                    int year = cal.get(Calendar.YEAR);
//
//                    if (!years.contains(year)) {
//                        years.add(year);
//                    }
//                }
//                Collections.sort(years);
//                return years;
//            }
//        } finally {
//            closeCursor(data);
//        }
//
//    }

//    public List<Trip> getTripsOfYearSortedByDate(int year) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_TRIPS + " WHERE " + COL_TRIPS_START_DATE +
//                " LIKE '%" + year + "' ", null);
//
//        int numRows = data.getCount();
//        if (numRows == 0) {
//            // empty list will be returned
//            return new ArrayList<>();
//        } else {
//            List<Trip> trips = new ArrayList<>();
//            while (data.moveToNext()) {
//                trips.add(new Trip(data));
//            }
//            Collections.sort(trips);
//            return trips;
//        }
//    }

//    public List<Trip> getTripsThatContainSpecificLocation(String country, String city) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_TRIPS +
//                " WHERE (" + COL_TRIPS_FROM_COUNTRY + " = '" + country + "' AND " + COL_TRIPS_FROM_CITY + " " +
//                " = '" + city + "') OR (" + COL_TRIPS_TO_COUNTRY + " = '" + country + "' AND " + COL_TRIPS_TO_CITY +
//                " = '" + city + "')", null);
//
//        int numRows = data.getCount();
//        try {
//            if (numRows == 0) {
//                return new ArrayList<>();
//            } else {
//                List<Trip> trips = new ArrayList<>();
//                while (data.moveToNext()) {
//                    trips.add(new Trip(data));
//                }
//                return trips;
//            }
//        } finally {
//            closeCursor(data);
//        }
//    }

//    public List<Trip> getAllTrips() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_TRIPS, null);
//
//        int numRows = data.getCount();
//        if (numRows == 0) {
//            return new ArrayList<>();
//        } else {
//            List<Trip> trips = new ArrayList<>();
//            while (data.moveToNext()) {
//                trips.add(new Trip(data));
//            }
//            return trips;
//        }
//    }

    public boolean isTripsTableEmpty() {
        return isTableEmpty(TABLE_TRIPS);
    }

    /*
     ********* TABLE CITY_LOC  **********************
     */

    public void addCityLocation(String country, String city, Float latitude, Float longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CITY_LOC_COUNTRY, country);
        contentValues.put(COL_CITY_LOC_CITY, city);
        contentValues.put(COL_CITY_LOC_LAT, latitude);
        contentValues.put(COL_CITY_LOC_LONG, longitude);

        db.insert(TABLE_CITY_LOC, null, contentValues);
    }

    public boolean isCityLocTableEmpty() {
        return isTableEmpty(TABLE_CITY_LOC);
    }

    public CityLocation getLocationOfCity(String country, String city) {
        List<CityLocation> cityLocations = getStoredCityCoordinates(country, city);
        if (cityLocations.isEmpty()) {
            return null;
        }
        return cityLocations.get(0);
    }

    public void saveCityLocationIfNotInDb(CityLocation cityLocation) {
        if (isCityLocationInDb(cityLocation)) {
            return;
        }
        addCityLocation(cityLocation.getCountry(), cityLocation.getCity(), cityLocation.getLatitude(), cityLocation.getLongitude());
    }

    private boolean isCityLocationInDb(CityLocation cityLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CITY_LOC + " WHERE " + COL_CITY_LOC_COUNTRY +
                " = '" + cityLocation.getCountry() + "' AND " + COL_CITY_LOC_CITY + " = '" + cityLocation.getCity() + "'", null);
        try {
            if (data.moveToNext()) {
                int rowCount = data.getInt(0);
                return rowCount >= 1;
            }
            return false;
        } finally {
            closeCursor(data);
        }
    }

    public List<CityLocation> getStoredCityCoordinates(String country, String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        String cityQuery = city.equals("") ? "%" : city;
        String countryQuery = country.equals("") ? "%" : country;

        Cursor data = db.rawQuery("SELECT " + COL_CITY_LOC_CITY + ", " + COL_CITY_LOC_COUNTRY +
                ", " + COL_CITY_LOC_LAT + ", " + COL_CITY_LOC_LONG + ", " + COL_CITY_LOC_ID +
                " FROM " + TABLE_CITY_LOC + " WHERE " + COL_CITY_LOC_COUNTRY + " LIKE '" + countryQuery + "' " +
                " AND " + COL_CITY_LOC_CITY + " LIKE '" + cityQuery + "'", null);

        int numRows = data.getCount();
        if (numRows == 0) {
            return new ArrayList<>();
        } else {
            List<CityLocation> cityCoordinates = new ArrayList<>();
            while (data.moveToNext()) {
                cityCoordinates.add(new CityLocation(data));
            }
            return cityCoordinates;
        }
    }

    public void updateCityLocation(CityLocation cityLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CITY_LOC + " SET " +
                COL_CITY_LOC_COUNTRY + " = '" + cityLocation.getCountry() + "'," +
                COL_CITY_LOC_CITY + " = '" + cityLocation.getCity() + "'," +
                COL_CITY_LOC_LAT + " = " + cityLocation.getLatitude() + "," +
                COL_CITY_LOC_LONG + " = " + cityLocation.getLongitude() + " " +
                " WHERE " + COL_CITY_LOC_ID + " = " + cityLocation.getId());
    }

    public List<CityLocation> getAllCityLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL_CITY_LOC_CITY + ", " + COL_CITY_LOC_COUNTRY +
                ", " + COL_CITY_LOC_LAT + ", " + COL_CITY_LOC_LONG + ", " + COL_CITY_LOC_ID +
                " FROM " + TABLE_CITY_LOC, null);

        int numRows = data.getCount();
        if (numRows == 0) {
            return new ArrayList<>();
        } else {
            List<CityLocation> cityLocations = new ArrayList<>();
            while (data.moveToNext()) {
                cityLocations.add(new CityLocation(data));
            }
            return cityLocations;
        }
    }

    /*
     ********* TABLE COUNTRIES **********************
     */

    public void addCountryContinentItem(String continent, String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_COUNTRIES_CONTINENT, continent);
        contentValues.put(COL_COUNTRIES_COUNTRY, country);

        db.insert(TABLE_COUNTRIES, null, contentValues);
    }

    public String getContinentOfCountry(String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL_COUNTRIES_CONTINENT + " FROM " + TABLE_COUNTRIES +
                " WHERE " + COL_COUNTRIES_COUNTRY + " = '" + country + "'", null);
        try {
            if (data.moveToNext()) {
                return data.getString(0);
            }
            return null;
        } finally {
            closeCursor(data);
        }
    }

    public boolean isCountriesTableEmpty() {
        return isTableEmpty(TABLE_COUNTRIES);
    }

    public List<String> getCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT DISTINCT " + COL_COUNTRIES_COUNTRY + " FROM " + TABLE_COUNTRIES,
                null);

        int numRows = data.getCount();
        try {
            if (numRows == 0) {
                // empty list will be returned
                return new ArrayList<>();
            } else {
                List<String> countries = new ArrayList<>();
                while (data.moveToNext()) {
                    countries.add(data.getString(0));
                }
                Collections.sort(countries);
                return countries;
            }
        } finally {
            closeCursor(data);
        }
    }

//    public List<CountriesContinents> getAllCountriesContinents() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_COUNTRIES, null);
//
//        int numRows = data.getCount();
//        if (numRows == 0) {
//            return new ArrayList<>();
//        } else {
//            List<CountriesContinents> countriesContinents = new ArrayList<>();
//            while (data.moveToNext()) {
//                countriesContinents.add(new CountriesContinents(data));
//            }
//            return countriesContinents;
//        }
//    }

    /*
     ********* STATISTICS / CHARTS **********************
     */

    public List<DataEntry> getTop10VisitedPlaces(List<String> years) {
        SQLiteDatabase db = this.getWritableDatabase();

        String yearQuery = buildYearQuery(years);
        Cursor data = db.rawQuery("SELECT " + COL_TRIPS_TO_CITY + ", COUNT(" + COL_TRIPS_TO_CITY + ") AS total" +
                " FROM (" +
                " SELECT " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY +
                " FROM " + TABLE_TRIPS +
                " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')" +
                yearQuery +
                " GROUP BY " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY +
                " ) AS cities" +
                " GROUP BY " + COL_TRIPS_TO_CITY +
                " ORDER BY total DESC" +
                " LIMIT 10", null);

        int numRows = data.getCount();
        try {
            if (numRows == 0) {
                return new ArrayList<>();
            } else {
                List<DataEntry> top10Places = new ArrayList<>();
                while (data.moveToNext()) {
                    top10Places.add(new ValueDataEntry(data.getString(0), data.getInt(1)));
                }
                return top10Places;
            }
        } finally {
            closeCursor(data);
        }
    }

    // SQlite does not support type Date, this is a workaround for filtering by trip year
    private String buildYearQuery(List<String> years) {
        if (years.isEmpty()) {
            return "";
        }
        StringBuilder query = new StringBuilder(" AND ((" + COL_TRIPS_START_DATE + " LIKE '%" + years.get(0) + "')");
        for (int i = 1; i < years.size(); i++) {
            query.append(" OR (" + COL_TRIPS_START_DATE + " LIKE '%").append(years.get(i)).append("')");
        }
        query.append(")");
        return query.toString();
    }

    public int getNumberOfVisitedPlaces() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT COUNT(DISTINCT(" + COL_TRIPS_TO_CITY + "))" +
                " FROM " + TABLE_TRIPS +
                " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')", null);
        try {
            if (data.moveToNext()) {
                return data.getInt(0);
            }
            return -1;
        } finally {
            closeCursor(data);
        }
    }

//    public List<DataEntry> getVisitedCountries() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery(
//                "SELECT " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT + ", COUNT(" + COL_TRIPS_TO_COUNTRY + ")" +
//                        " FROM (" +
//                        " SELECT " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT +
//                        " FROM " + TABLE_TRIPS +
//                        " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')" +
//                        " GROUP BY " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT +
//                        " ) AS countries" +
//                        " GROUP BY " + COL_TRIPS_TO_COUNTRY + ", " + COL_TRIPS_CONTINENT +
//                        " ORDER BY " + COL_TRIPS_CONTINENT, null);
//
//        int numRows = data.getCount();
//        try {
//            if (numRows == 0) {
//                return new ArrayList<>();
//            } else {
//                List<DataEntry> visitedCountries = new ArrayList<>();
//                while (data.moveToNext()) {
//                    visitedCountries.add(new ChartHelper.CustomCategoryValueDataEntry(
//                            data.getString(0), data.getString(1), data.getInt(2)));
//                }
//
//                for (DataEntry dataEntry : visitedCountries) {
//                    ChartHelper.CustomCategoryValueDataEntry customEntry = (ChartHelper.CustomCategoryValueDataEntry) dataEntry;
//                    String visitedCountry = (String) customEntry.getValue("x");
//                    Cursor data2 = db.rawQuery(
//                            "SELECT * FROM " + TABLE_TRIPS + " WHERE "
//                                    + COL_TRIPS_TO_COUNTRY + " = '" + visitedCountry + "' " +
//                                    " AND " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')", null);
//                    List<Trip> trips = new ArrayList<>();
//                    while (data2.moveToNext()) {
//                        trips.add(new Trip(data2));
//                    }
//
//                    customEntry.setTripsInfo(trips);
//                    closeCursor(data2);
//                }
//                return visitedCountries;
//            }
//        } finally {
//            closeCursor(data);
//        }
//    }

    public int getNumberOfVisitedCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT COUNT(DISTINCT(" + COL_TRIPS_TO_COUNTRY + "))" +
                " FROM " + TABLE_TRIPS +
                " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')", null);
        try {
            if (data.moveToNext()) {
                return data.getInt(0);
            }
            return -1;
        } finally {
            closeCursor(data);
        }
    }

    public List<DataEntry> getVisitedPlaces() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT + ", COUNT(" + COL_TRIPS_TO_CITY + ")" +
                        " FROM (" +
                        " SELECT " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT +
                        " FROM " + TABLE_TRIPS +
                        " WHERE " + COL_TRIPS_TYPE + " NOT IN ('MULTI_STOP_LAST_STOP')" +
                        " GROUP BY " + COL_TRIPS_GRP_ID + ", " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT +
                        " ) AS cities" +
                        " GROUP BY " + COL_TRIPS_TO_CITY + ", " + COL_TRIPS_CONTINENT +
                        " ORDER BY " + COL_TRIPS_CONTINENT, null);

        int numRows = data.getCount();
        try {
            if (numRows == 0) {
                return new ArrayList<>();
            } else {
                List<DataEntry> visitedPlaces = new ArrayList<>();
                while (data.moveToNext()) {
                    visitedPlaces.add(new CategoryValueDataEntry(
                            data.getString(0), data.getString(1), data.getInt(2)));
                }
                return visitedPlaces;
            }
        } finally {
            closeCursor(data);
        }
    }


//    public List<DataEntry> getKmsPerContinentPerYear() {
//        List<Integer> allYears = getAllYearsOfTrips();
//
//        List<DataEntry> areaChartList = new ArrayList<>();
//
//        for (int year : allYears) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor data = db.rawQuery(
//                    "SELECT " + COL_TRIPS_CONTINENT + ", SUM(" + COL_TRIPS_DISTANCE + ")" +
//                            " FROM " + TABLE_TRIPS +
//                            " WHERE " + COL_TRIPS_START_DATE + " LIKE '%" + year + "'" +
//                            " GROUP BY " + COL_TRIPS_CONTINENT, null);
//            int numRows = data.getCount();
//            try {
//                if (numRows == 0) {
//                    continue;
//                }
//                Map<String, Integer> continentsAndKmsMap = new HashMap<>();
//                while (data.moveToNext()) {
//                    continentsAndKmsMap.put(data.getString(0), data.getInt(1));
//                }
//
//                areaChartList.add(new ChartHelper.CustomDataEntry(
//                        Integer.toString(year),
//                        getKmsPerContinentList(continentsAndKmsMap)
//                ));
//            } finally {
//                closeCursor(data);
//            }
//        }
//        return areaChartList;
//
//    }

    private List<Integer> getKmsPerContinentList(Map<String, Integer> continentsAndKmsMap) {
        List<Integer> kmsPerContinentList = new ArrayList<>();
        for (String continent : CONTINENTS) {
            kmsPerContinentList.add(continentsAndKmsMap.getOrDefault(continent, 0));
        }
        return kmsPerContinentList;
    }


//    public List<DataEntry> getKmsAndTripsPerYear() {
//        List<Integer> allYears = getAllYearsOfTrips();
//        List<DataEntry> bubbleChartList = new ArrayList<>();
//
//        for (int year : allYears) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor data = db.rawQuery(
//                    "SELECT SUM(" + COL_TRIPS_DISTANCE + "), COUNT(DISTINCT(" + COL_TRIPS_GRP_ID + "))" +
//                            " FROM " + TABLE_TRIPS +
//                            " WHERE " + COL_TRIPS_START_DATE + " LIKE '%" + year + "'", null);
//            Cursor data2 = db.rawQuery(
//                    "SELECT DISTINCT(" + COL_TRIPS_TO_COUNTRY + ")" +
//                            " FROM " + TABLE_TRIPS +
//                            " WHERE " + COL_TRIPS_START_DATE + " LIKE '%" + year + "'", null);
//
//            int numRows2 = data2.getCount();
//            List<String> countries = new ArrayList<>();
//            try {
//                if (numRows2 == 0) {
//                    continue;
//                }
//                while (data2.moveToNext()) {
//                    countries.add(data2.getString(0));
//                }
//            } finally {
//                closeCursor(data2);
//            }
//
//            int numRows = data.getCount();
//            try {
//                if (numRows == 0) {
//                    continue;
//                }
//
//                while (data.moveToNext()) {
//                    bubbleChartList.add(new ChartHelper.CustomBubbleDataEntry(
//                            year,
//                            data.getInt(1),
//                            data.getInt(0),
//                            countries
//                    ));
//                }
//            } finally {
//                closeCursor(data);
//            }
//        }
//
//        return bubbleChartList;
//    }

    public int getTotalTravelledKms() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT SUM(" + COL_TRIPS_DISTANCE + ")" +
                " FROM " + TABLE_TRIPS, null);
        try {
            if (data.moveToNext()) {
                return data.getInt(0);
            }
            return -1;
        } finally {
            closeCursor(data);
        }
    }

    public List<CityLocation> getLatitudeAndLongitudeOfVisitedCities() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT DISTINCT(" + TABLE_TRIPS + "." + COL_TRIPS_TO_CITY + ")" +
                        ", " + TABLE_TRIPS + "." + COL_TRIPS_TO_COUNTRY +
                        ", " + TABLE_CITY_LOC + "." + COL_CITY_LOC_LAT +
                        ", " + TABLE_CITY_LOC + "." + COL_CITY_LOC_LONG +
                        ", " + TABLE_CITY_LOC + "." + COL_CITY_LOC_ID +
                        " FROM " + TABLE_TRIPS +
                        " INNER JOIN " + TABLE_CITY_LOC +
                        " ON " + TABLE_TRIPS + "." + COL_TRIPS_TO_CITY + " = " + TABLE_CITY_LOC + "." + COL_CITY_LOC_CITY +
                        " AND " + TABLE_TRIPS + "." + COL_TRIPS_TO_COUNTRY + " = " + TABLE_CITY_LOC + "." + COL_CITY_LOC_COUNTRY +
                        " ORDER BY " + TABLE_TRIPS + "." + COL_TRIPS_TO_CITY, null);

        int numRows = data.getCount();
        if (numRows == 0) {
            return new ArrayList<>();
        }
        List<CityLocation> latAndLongOfVisitedCities = new ArrayList<>();
        while (data.moveToNext()) {
            latAndLongOfVisitedCities.add(new CityLocation(data));
        }
        return latAndLongOfVisitedCities;
    }


    /*
     ********* GENERAL **********************
     */


    private boolean isTableEmpty(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
        try {
            if (data.moveToNext()) {
                int rowCount = data.getInt(0);
                // to consider all possible return values of count (0, -1, etc.)
                return rowCount < 1;
            }
            return true;
        } finally {
            closeCursor(data);
        }
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
