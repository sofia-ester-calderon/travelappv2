package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ChartMenuItem(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit,
)

@Composable
fun ChartMenu(
    menuItems: List<ChartMenuItem>,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        var expanded by remember { mutableStateOf(false) }

        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            menuItems.forEach {
                DropdownMenuItem(onClick = {
                    expanded = false
                    it.onClick()
                }) {
                    TopTenMenuItem(text = it.text, selected = it.selected)
                }
            }
        }
    }
}

@Composable
private fun TopTenMenuItem(text: String, selected: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text)
        if (selected) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                modifier = Modifier.height(16.dp)
            )
        }

    }
}