package com.sucaldo.travelappv2.features.citycoordinates.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.CityLocation

@Composable
fun LocationList(locations: List<CityLocation>, onOpenCityLocation: (CityLocation) -> Unit) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        item {
            LocationRow(
                item1 = stringResource(id = R.string.common_country),
                item2 = stringResource(id = R.string.common_city),
                item3 = stringResource(id = R.string.common_latitude),
                item4 = stringResource(id = R.string.common_longitude),
                fontWeight = FontWeight.Bold,
            )
        }
        items(items = locations) { location ->
            LocationRow(
                Modifier.clickable {
                    onOpenCityLocation(location)
                },
                location.country,
                location.city,
                location.latitude.toString(),
                location.longitude.toString()
            )
        }
    }
}

@Composable
private fun LocationRow(
    modifier: Modifier = Modifier,
    item1: String,
    item2: String,
    item3: String,
    item4: String,
    fontWeight: FontWeight? = null,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = item1, modifier = Modifier.weight(1f), fontWeight = fontWeight)
        Text(text = item2, modifier = Modifier.weight(1f), fontWeight = fontWeight)
        Text(text = item3, modifier = Modifier.weight(1f), fontWeight = fontWeight)
        Text(text = item4, modifier = Modifier.weight(1f), fontWeight = fontWeight)
    }
}