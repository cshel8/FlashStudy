package com.example.flashstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.flashstudy.data.settings.UserSettingsRepository
import com.example.flashstudy.ui.navigation.App
import com.example.flashstudy.ui.screen.DeckListScreen
import com.example.flashstudy.ui.theme.FlashStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashStudyAppRoot()
            }
        }
    }

@Composable
fun FlashStudyAppRoot() {
    val context = LocalContext.current

    val settingsRepository = remember {
        UserSettingsRepository( context )
    }
    val darkTheme by settingsRepository
        .darkTheme
        .collectAsState( initial = false )
    val colorblindAssist by settingsRepository
        .colorBlindAssist
        .collectAsState( initial = false )

    FlashStudyTheme(
        darkTheme = darkTheme,
        colorblindAssist = colorblindAssist
    ) {
        App(
            settingsRepository = settingsRepository
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlashStudyTheme {
        Greeting("Android")
    }
}