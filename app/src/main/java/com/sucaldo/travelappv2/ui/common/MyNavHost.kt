package com.sucaldo.travelappv2.ui.common

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sucaldo.travelappv2.ui.home.HomeScreen
import com.sucaldo.travelappv2.ui.settings.SettingsScreen
import com.sucaldo.travelappv2.ui.trip.ui.TripScreen

@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    val homeRoute = Routes.HOME
    val tripRoute = Routes.TRIP
    val tripRouteTripId = Routes.TRIP_ROUTE_ID
    val settingsRoute = Routes.SETTINGS
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
            route = buildRouteWithArgument(
                route = tripRoute,
                argument = tripRouteTripId,
            ),
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
            route = settingsRoute
        ) {
            SettingsScreen(navController)
        }
    }
}

private fun buildRouteWithArgument(route: String, argument: String): String {
    return "$route?$argument={$argument}"
}

object Routes {
    const val HOME = "home"
    const val TRIP = "trip"
    const val TRIP_ROUTE_ID = "tripId"
    const val SETTINGS = "settings"
}

