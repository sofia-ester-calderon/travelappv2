package com.sucaldo.travelappv2.features.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopBar(
    navController: NavController,
    title: String,
) {
    val settingsRoute = Routes.SETTINGS
    Column {
        TopAppBar(
            elevation = 4.dp,
            title = {
                Text(text = title)
            },
            backgroundColor = MaterialTheme.colors.primarySurface,
            navigationIcon = {
                DropdownMenu(navController)
            }, actions = {
                IconButton(onClick = {
                    navController.navigate(settingsRoute)
                }) {
                    Icon(Icons.Filled.Settings, null)
                }
            })
    }
}