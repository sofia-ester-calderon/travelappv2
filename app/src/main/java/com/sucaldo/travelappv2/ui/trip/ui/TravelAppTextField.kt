package com.sucaldo.travelappv2.ui.trip.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

@Composable
fun TravelAppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorText: String? = null,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onClickIcon: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        label = { Text(label) },
        isError = errorText != null,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        trailingIcon = {
            if (icon != null) {
                IconButton(
                    onClick = onClickIcon!!,
                ) {
                    Icon(
                        icon,
                        contentDescription = iconDescription,
                        tint = if(enabled) Color.Black else Color.Gray
                    )
                }
            }
        }
    )
    if (errorText != null) {
        ErrorText(text = errorText)
    }
}

@Composable
fun ErrorText(text: String) {
    Text(text = text, fontSize = 12.sp, color = Color.Red)

}
