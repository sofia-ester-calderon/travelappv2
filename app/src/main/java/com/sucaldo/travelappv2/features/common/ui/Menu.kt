package com.sucaldo.travelappv2.features.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sucaldo.travelappv2.R

@Composable
fun DropdownMenu(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

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
            MenuRow(icon = Icons.Outlined.FavoriteBorder, textId = R.string.title_statistics) {
                navController.navigate(Routes.STATISTICS)
            }
            MenuRow(icon = Icons.Outlined.Place, textId = R.string.title_world_map) {
                navController.navigate(Routes.WORLD_MAP)
            }
            MenuRow(icon = Icons.Outlined.Add, textId = R.string.title_new_trip) {
                navController.navigate(Routes.TRIP)
            }
            MenuRow(icon = Icons.Outlined.List, textId = R.string.title_trips) {
                navController.navigate(Routes.TRIPS)
            }
            MenuRow(icon = Icons.Outlined.LocationOn, textId = R.string.title_city_coordinates, isLast = true) {
                navController.navigate(Routes.CITY_COORDINATES)
            }

        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    textId: Int,
    isLast: Boolean = false,
    onNavigate: () -> Unit
) {
    DropdownMenuItem(onClick = onNavigate) {
        Row {
            Icon(
                icon,
                contentDescription = stringResource(textId)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(textId))
        }
    }
    if (!isLast) {
        Divider()
    }
}