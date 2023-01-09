package com.sucaldo.travelappv2.features.common.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sucaldo.travelappv2.features.citycoordinates.ui.CityCoordinatesScreen
import com.sucaldo.travelappv2.features.home.HomeScreen
import com.sucaldo.travelappv2.features.settings.ui.SettingsScreen
import com.sucaldo.travelappv2.features.statistics.ui.StatisticsScreen
import com.sucaldo.travelappv2.features.trip.ui.TripScreen
import com.sucaldo.travelappv2.features.trips.ui.TripsScreen

@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    val homeRoute = Routes.HOME
    val tripRouteTripId = Routes.TRIP_ROUTE_ID
    NavHost(
        navController = navController,
        startDestination = homeRoute,
    ) {
        composable(
            route = homeRoute
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            "${Routes.TRIP}?$tripRouteTripId={$tripRouteTripId}",
            arguments = listOf(navArgument(tripRouteTripId) {
                nullable = true
            })
        ) {
            TripScreen(
                navController = navController,
                tripId = it.arguments?.getString(tripRouteTripId)
            )
        }
        composable(
            route = Routes.SETTINGS
        ) {
            SettingsScreen(navController)
        }
        composable(
            route = Routes.CITY_COORDINATES
        ) {
            CityCoordinatesScreen(navController)
        }
        composable(
            route = Routes.TRIPS
        ) {
            TripsScreen(navController)
        }
        composable(
            route = Routes.STATISTICS
        ) {
            StatisticsScreen(navController)
        }
    }
}

object Routes {
    const val HOME = "home"
    const val TRIP = "trip"
    const val TRIP_ROUTE_ID = "tripId"
    const val SETTINGS = "settings"
    const val CITY_COORDINATES = "cityCoordinates"
    const val TRIPS = "trips"
    const val STATISTICS = "statistics"
}

