package com.example.flashstudy.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteDialog(
    title: String = "Delete Item",
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text( text = title )
        },
        text = {
            Text( text = message )
        },
        confirmButton = {
            Button( onClick = onConfirm ) {
                Text( "Delete" )
            }
        },
        dismissButton = {
            Button( onClick = onDismiss ) {
                Text( "Cancel" )
            }
        }
    )
}