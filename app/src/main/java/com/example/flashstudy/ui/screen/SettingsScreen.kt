package com.example.flashstudy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    shuffleCards: Boolean,
    onShuffleChange: ( Boolean ) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding( 16.dp )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.align( Alignment.CenterStart )
            ) {
                Text( "Back" )
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align( Alignment.Center )
            )
        }
        Column(
            modifier = Modifier.padding( top = 24.dp ),
            verticalArrangement = Arrangement.spacedBy( 16.dp )
        ) {
            Text( "Appearance" )
            Text( "Theme: Light / Dark" )

            Text(
                text = "Study Preferences",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text( "Shuffle cards" )
                Switch(
                    checked = shuffleCards,
                    onCheckedChange = onShuffleChange
                )
            }

            Text( "Account" )
            Text( "Sign in / Cloud sync" )
        }
    }
}