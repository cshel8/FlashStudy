package com.example.flashstudy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.flashstudy.data.local.DatabaseProvider
import com.example.flashstudy.data.local.RoomDeckRepository
import com.example.flashstudy.data.local.RoomFlashcardRepository
import com.example.flashstudy.data.settings.UserSettingsRepository
import com.example.flashstudy.ui.screen.DeckListScreen
import com.example.flashstudy.ui.screen.DeckScreen
import com.example.flashstudy.ui.screen.StudyScreen
import com.example.flashstudy.ui.screen.SettingsScreen
import com.example.flashstudy.ui.screen.LoginScreen
import com.google.firebase.auth.FirebaseAuth

fun MutableList<Screen>.popToRoot() {
    while ( size > 1 ) {
        removeAt( size - 1 )
    }
}
fun MutableList<Screen>.safePop() {
    if ( size > 1 ) {
        removeAt( size - 1 )
    }
}

@Composable
fun App(
    settingsRepository: UserSettingsRepository
) {
    val context = LocalContext.current

    val database = remember {
        DatabaseProvider.getDatabase( context )
    }
    val repository = remember {
        RoomDeckRepository( database.deckDao() )
    }
    val flashcardRepository = remember {
        RoomFlashcardRepository( database.flashcardDao() )
    }
    val scope = rememberCoroutineScope()
    val shuffleCards by settingsRepository
        .shuffleCards
        .collectAsState( initial = false )
    val darkTheme by settingsRepository
        .darkTheme
        .collectAsState( initial = false )
    val colorblindAssist by settingsRepository
        .colorBlindAssist
        .collectAsState( initial = false )
    val backStack = remember {
        mutableStateListOf< Screen >( Screen.DeckList ) }
    val auth = remember {
        FirebaseAuth.getInstance()
    }
    when ( val screen = backStack.last() ) {
        Screen.DeckList -> {
            DeckListScreen(
                repository = repository,
                flashcardRepository = flashcardRepository,
                onDeckClick = { id ->
                    backStack.add( Screen.Deck( id ))
                },
                onStudyClick = { id ->
                    backStack.add( Screen.Deck( id ) )
                    backStack.add( Screen.Study( id ) )
                },
                onSettingsClick = {
                    backStack.add( Screen.Settings )
                }
            )
        }
        is Screen.Deck -> {
            DeckScreen(
                deckId = screen.deckId,
                repository = repository,
                flashcardRepository = flashcardRepository,
                onStartStudy = {
                    backStack.add( Screen.Study( screen.deckId ))
                },
                onBack = {
                    backStack.safePop()
                }
            )
        }
        is Screen.Study -> {
            val deck by repository
                .getDeckById( screen.deckId )
                .collectAsState( initial = null )
            StudyScreen(
                deckId = screen.deckId,
                deckName = deck?.name ?: "Unknown Deck",
                flashcardRepository = flashcardRepository,
                shuffleCards = shuffleCards,
                onBack = {
                    backStack.safePop()
                },
                onHome = {
                    backStack.popToRoot()
                }
            )
        }
        Screen.Settings -> {
            SettingsScreen(
                shuffleCards = shuffleCards,
                darkTheme = darkTheme,
                colorblindAssist = colorblindAssist,
                onShuffleChange = { enabled ->
                    scope.launch {
                        settingsRepository.setShuffleCards( enabled )
                    }
                },
                onDarkThemeChange = { enabled ->
                    scope.launch {
                        settingsRepository.setDarkTheme( enabled )
                    }
                },
                onColorblindAssistChange = { enabled ->
                    scope.launch {
                        settingsRepository.setColorblindAssist( enabled )
                    }
                },
                onLoginClick = {
                    backStack.add( Screen.Login )
                },
                onBack = {
                    backStack.safePop()
                }
            )
        }
        Screen.Login -> {
            LoginScreen(
                onLoginClick = { email, password ->
                    auth.signInWithEmailAndPassword( email, password )
                        .addOnSuccessListener {
                            backStack.safePop()
                        }
                        .addOnFailureListener { error ->
                            println( "Login failed: ${error.message}")
                        }
                },
                onSignUpClick = { email, password ->
                    auth.createUserWithEmailAndPassword( email, password )
                        .addOnSuccessListener {
                            backStack.safePop()
                        }
                        .addOnFailureListener { error ->
                            println( "Sign up failed: ${ error.message }")

                        }
                },
                onBack = {
                    backStack.safePop()
                }
            )
        }
    }
}