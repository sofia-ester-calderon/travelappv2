package com.sucaldo.travelappv2.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sucaldo.travelappv2.R
import com.sucaldo.travelappv2.data.FieldErrorType

@Composable
fun getErrorText(fieldErrorType: FieldErrorType): String? {
    return when(fieldErrorType) {
        FieldErrorType.EMPTY -> stringResource(id = R.string.field_error_empty)
        FieldErrorType.INVALID_DATE_FORMAT -> stringResource(id = R.string.field_error_invalid_date_format)
        FieldErrorType.INVALID_END_DATE -> stringResource(id = R.string.field_error_invalid_end_date)
        FieldErrorType.INVALID_START_DATE -> stringResource(id = R.string.field_error_invalid_start_date)
        FieldErrorType.LAT_LONG_DB -> stringResource(id = R.string.field_error_db)
        FieldErrorType.INVALID_CHARS -> stringResource(id = R.string.field_error_invalid_chars)
        FieldErrorType.LOCATION_DB -> stringResource(id = R.string.field_error_location_db)
        FieldErrorType.NONE -> null
    }
}