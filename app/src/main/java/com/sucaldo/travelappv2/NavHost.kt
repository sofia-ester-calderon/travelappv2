package com.sucaldo.travelappv2

//@Composable
//fun MyAppNavHost(
//    modifier: Modifier = Modifier,
//    navController: NavHostController = rememberNavController(),
//    startDestination: String = "newTrip"
//) {
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        /* creating route "home" */
//        composable(route = "newTrip") {
//            NewTripScreen(navController)
//        }
//        composable(
//            "settings?userId={userId}",
//            arguments = listOf(navArgument("userId") { defaultValue = "user1234" })
//        ) { backStackEntry ->
//            SettingsScreen(navController, backStackEntry.arguments?.getString("userId"))
//        }
//    }
//}

