package com.example.flashstudy.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LimitCounter(
    current: Int,
    limit: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    val color = when {
        current >= limit -> MaterialTheme.colorScheme.error
        current >= limit - 2 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = "$label: $current / $limit",
        color = color,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}