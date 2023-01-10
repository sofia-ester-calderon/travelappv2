package com.sucaldo.travelappv2.features.statistics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChartView
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.features.statistics.TopPlacesType


@Composable
fun TopTenChart(
    topPlacesType: TopPlacesType,
    onInitChart: (AnyChartView) -> Unit,
    onUpdateChart: (TopPlacesType) -> Unit
) {
    TopTenSelection(
        topPlacesType = topPlacesType,
        onUpdateChart = onUpdateChart
    )
    AndroidView(
        factory = {
            val chart = AnyChartView(it)
            onInitChart(chart)
            chart
        }, modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun TopTenSelection(
    topPlacesType: TopPlacesType,
    onUpdateChart: (TopPlacesType) -> Unit
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
                onUpdateChart(TopPlacesType.OVERALL)
            }) {
                TopTenMenuItem(
                    text = stringResource(id = R.string.trips_statistics_top_ten_overall),
                    selected = topPlacesType == TopPlacesType.OVERALL
                )
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(TopPlacesType.LAST_TWO)
            }) {
                TopTenMenuItem(
                    text = stringResource(id = R.string.trips_statistics_top_ten_last_two),
                    selected = topPlacesType == TopPlacesType.LAST_TWO
                )
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(TopPlacesType.LAST_FIVE)
            }) {
                TopTenMenuItem(
                    text = stringResource(id = R.string.trips_statistics_top_ten_last_five),
                    selected = topPlacesType == TopPlacesType.LAST_FIVE
                )
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onUpdateChart(TopPlacesType.LAST_TEN)
            }) {
                TopTenMenuItem(
                    text = stringResource(id = R.string.trips_statistics_top_ten_last_ten),
                    selected = topPlacesType == TopPlacesType.LAST_TEN
                )
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
