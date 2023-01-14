package com.sucaldo.travelappv2.features.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TravelAppDialog(onDismissDialog: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Dialog(onDismissRequest = onDismissDialog) {
        Surface(elevation = 9.dp) {
            Column(
                Modifier.padding(16.dp),
                content = content
            )
        }
    }
}