package com.sucaldo.travelappv2.ui.newtrip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.ui.common.TopBar

@Composable
fun TripScreen(navController: NavController, tripId: String?) {
    val title = if (tripId == null) {
        stringResource(id = R.string.title_new_trip)
    } else {
        stringResource(id = R.string.title_edit_trip)
    }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = title,
            )
        }
    ) {
        Box(modifier = androidx.compose.ui.Modifier.padding(it)) {
            Column {
                Text(text = "Trip")
                Text(text = tripId ?: "NEW")
                Button(onClick = { navController.navigate("settings?userId=something") }) {
                    Text(text = "Navigate with arg")
                }
                Button(onClick = { navController.navigate("settings") }) {
                    Text(text = "Navigate without arg")
                }
            }
        }
    }


}
