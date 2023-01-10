package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChartView


@Composable
fun TopTenChart(
    onInitChart: (AnyChartView) -> Unit,
    onUpdateChart: (Int?) -> Unit
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
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(null)
            }) {
                Text(text = "Overall")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(2)
            }) {
                Text(text = "Last 2 years")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(5)
            }) {
                Text(text = "Last 5 years")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(10)
            }) {
                Text(text = "Last 10 years")
            }
        }
    }
    AndroidView(
        factory = {
            val chart = AnyChartView(it)
            onInitChart(chart)
            chart
        }, modifier = Modifier
            .fillMaxSize()
    )
}
