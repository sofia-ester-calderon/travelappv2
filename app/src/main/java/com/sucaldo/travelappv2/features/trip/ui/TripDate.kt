package com.sucaldo.travelappv2.features.trip.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.FieldErrorType
import com.sucaldo.travelappv2.features.common.ui.TravelAppTextField
import com.sucaldo.travelappv2.features.common.ui.getErrorText
import java.util.*

@Composable
fun TripDate(
    startDate: String,
    endDate: String,
    isEndDateEnabled: Boolean,
    startDateError: FieldErrorType,
    endDateError: FieldErrorType,
    onChangeStartDate: (String) -> Unit,
    onChangeEndDate: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row {
            Column(
                modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp / 2f),
            ) {
                DateField(
                    label = stringResource(id = R.string.trip_label_start),
                    date = startDate,
                    errorType = startDateError,
                    onChangeDate = onChangeStartDate,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                DateField(
                    label = stringResource(id = R.string.trip_label_end),
                    date = endDate,
                    enabled = isEndDateEnabled,
                    errorType = endDateError,
                    onChangeDate = onChangeEndDate,
                )
            }
        }
    }
}

@Composable
private fun DateField(
    label: String,
    date: String,
    enabled: Boolean = true,
    errorType: FieldErrorType,
    onChangeDate: (String) -> Unit,
) {
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date()
    val pickerYear = mCalendar.get(Calendar.YEAR)
    val pickerMonth = mCalendar.get(Calendar.MONTH)
    val pickerDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            onChangeDate("$mDayOfMonth.${mMonth + 1}.$mYear")
        }, pickerYear, pickerMonth, pickerDay
    )
    Column {
        TravelAppTextField(
            label = label,
            value = date,
            onValueChange = { onChangeDate(it) },
            icon = Icons.Default.DateRange,
            onClickIcon = { mDatePickerDialog.show() },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = enabled,
            errorText = getErrorText(fieldErrorType = errorType)
        )

    }
}
