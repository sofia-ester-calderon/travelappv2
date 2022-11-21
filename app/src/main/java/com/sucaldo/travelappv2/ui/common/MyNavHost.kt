package com.sucaldo.travelappv2.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.home.HomeScreen
import com.sucaldo.travelappv2.ui.newtrip.TripScreen
import com.sucaldo.travelappv2.ui.settings.SettingsScreen

@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    val homeRoute = getRoute(RouteType.HOME)
    val tripRoute = getRoute(RouteType.TRIP)
    val tripRouteTripId = stringResource(id = R.string.navigation_trip_tripId)
    val settingsRoute = getRoute(routeType = RouteType.SETTINGS)
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

@Composable
fun getRoute(routeType: RouteType): String {
    return when(routeType) {
        RouteType.HOME -> stringResource(id = R.string.navigation_home)
        RouteType.TRIP -> stringResource(id = R.string.navigation_trip)
        RouteType.SETTINGS -> stringResource(id = R.string.navigation_settings)
    }
}

enum class RouteType {
    HOME, TRIP, SETTINGS
}
