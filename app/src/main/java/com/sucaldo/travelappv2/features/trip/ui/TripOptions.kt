package com.sucaldo.travelappv2.features.trip.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.TripType


data class TripOptionsUi(
    val tripType: TripType,
    val label: String,
)

@Composable
fun TripOptions(
    tripType: TripType,
    onChangeTripType: (TripType) -> Unit,
) {
    val tripOptions = listOf(
        TripOptionsUi(
            label = stringResource(id = R.string.trip_options_return),
            tripType = TripType.RETURN,
        ),
        TripOptionsUi(
            label = stringResource(id = R.string.trip_options_one_way),
            tripType = TripType.ONE_WAY,
        ),
        TripOptionsUi(
            label = stringResource(id = R.string.trip_options_multi),
            tripType = TripType.MULTI_STOP,
        ),
    )

    Column {
        tripOptions.forEach {
            Row(
                Modifier
                    .selectable(
                        selected = (it.tripType == tripType),
                        onClick = { onChangeTripType(it.tripType) }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (it.tripType == tripType),
                    onClick = { onChangeTripType(it.tripType) },
                )
                Text(
                    text = it.label,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}