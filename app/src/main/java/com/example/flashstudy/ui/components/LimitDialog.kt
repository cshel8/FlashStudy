package com.example.flashstudy.ui.components

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LimitDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text( title ) },
        text = { Text( message ) },
        confirmButton = {
            Button( onClick = onDismiss ) {
                Text( "OK" )
            }
        }
    )
}