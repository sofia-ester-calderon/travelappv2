package com.sucaldo.travelappv2.features.common.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BigLabel(text: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.height(8.dp))
}

