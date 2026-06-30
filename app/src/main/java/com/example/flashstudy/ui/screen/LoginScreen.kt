package com.example.flashstudy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation



@Composable
fun LoginScreen(
    onLoginClick: ( String, String ) -> Unit,
    onSignUpClick: ( String, String ) -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf( "" ) }
    var password by remember { mutableStateOf( "" ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding( 24.dp ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FlashStudy",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Sign in to sync your decks",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text( "Email" ) }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text( "Password" ) },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                onLoginClick( email, password )
            }
        ) {
            Text( "Log In" )
        }
        TextButton(
            onClick = {
                onSignUpClick( email, password )
            }
        ) {
            Text( "Create Account" )
        }
        TextButton(
            onClick = onBack
        ) {
            Text( "Back" )
        }
    }
}