package com.example.flashstudy.ui.screen

import android.graphics.Paint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
    darkTheme: Boolean,
    colorblindAssist: Boolean,
    onShuffleChange: ( Boolean ) -> Unit,
    onDarkThemeChange: ( Boolean ) -> Unit,
    onColorblindAssistChange: ( Boolean ) -> Unit,
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
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium
                )
            Spacer( modifier = Modifier.height( 8.dp )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text( "Light / Dark Theme" )
                    Text(
                        text = if ( darkTheme ) "Dark Mode" else "Light Mode",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = darkTheme,
                    onCheckedChange = onDarkThemeChange
                )
            }
            Spacer( modifier = Modifier.height( 24.dp ) )
            Text(
                text = "Accessibility",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer( modifier = Modifier.height( 8.dp ) )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text( "Colorblind Assist" )
                    Text(
                        text = "Uses clearer visual cues",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = colorblindAssist,
                    onCheckedChange = onColorblindAssistChange
                )
            }
            Spacer( modifier = Modifier.height( 24.dp ) )
            Text(
                text = "Study Preferences",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer( modifier = Modifier.height( 8.dp ) )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text( "Shuffle Cards" )
                    Text(
                        text = "Randomize cards during study",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = shuffleCards,
                    onCheckedChange = onShuffleChange
                )
            }
            Spacer( modifier = Modifier.height( 24.dp ) )
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium
            )
            Text( "Sign in / Cloud sync" )
        }
    }
}