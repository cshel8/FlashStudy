package com.example.flashstudy.ui.navigation

sealed class Screen {
    data object DeckList : Screen()
    data class Deck( val deckId: Int ) : Screen()
    data class Study( val deckId: Int ) : Screen()
    data object  Settings : Screen()

    data object Login : Screen()
}