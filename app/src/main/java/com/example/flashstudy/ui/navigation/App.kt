package com.example.flashstudy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.flashstudy.data.FakeDeckRepository
import com.example.flashstudy.data.FakeFlashcardRepository
import com.example.flashstudy.ui.screen.DeckListScreen
import com.example.flashstudy.ui.screen.DeckScreen
import com.example.flashstudy.ui.screen.StudyScreen

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
fun App() {
    val repository = remember { FakeDeckRepository() }
    val flashcardRepository = remember { FakeFlashcardRepository() }
    val backStack = remember {
        mutableStateListOf< Screen >( Screen.DeckList ) }
    when ( val screen = backStack.last() ) {
        Screen.DeckList -> {
            DeckListScreen(
                repository = repository,
                onDeckClick = { id ->
                    backStack.add( Screen.Deck( id ))
                },
                onAddDeck = {
                    repository.addDeck( "New Deck" )
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
            val deck = repository.getDeckById( screen.deckId )
            StudyScreen(
                deckId = screen.deckId,
                deckName = deck?.name ?: "Unknown Deck",
                flashcardRepository = flashcardRepository,
                onBack = {
                    backStack.safePop()
                },
                onHome = {
                    backStack.popToRoot()
                }
            )
        }
    }
}