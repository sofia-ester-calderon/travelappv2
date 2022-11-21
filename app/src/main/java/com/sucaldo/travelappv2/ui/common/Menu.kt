package com.sucaldo.travelappv2.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R

@Composable
fun DropdownMenu(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val tripRoute = getRoute(routeType = RouteType.TRIP)
    val homeRoute = getRoute(routeType = RouteType.HOME)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.menu))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                navController.navigate(homeRoute)
            }) {
                Row {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                    Text(stringResource(id = R.string.title_home))

                }
            }
            Divider()
            DropdownMenuItem(onClick = {
                navController.navigate(tripRoute)
            }) {
                Row {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                    Text(stringResource(id = R.string.title_new_trip))

                }
            }
        }
    }
}