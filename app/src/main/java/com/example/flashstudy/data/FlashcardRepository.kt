package com.example.flashstudy.data

import androidx.compose.runtime.mutableStateListOf

data class Flashcard(
    val id: Int,
    val deckId: Int,
    val question: String,
    val answer: String
)

class FakeFlashcardRepository {
    private val cards = mutableStateListOf(
        Flashcard( 1, 1, "Hola", "Hello" ),
        Flashcard( 2, 2, "WW2 ended?", "1945" ),
        Flashcard( 3, 3, "What planet is known as the Red Planet?", "Mars" ),

    )
    fun getCardsForDeck( deckId: Int ): List<Flashcard> {
        return cards.filter { it.deckId == deckId }
    }
    fun addCard( deckId: Int, question: String, answer: String) {
        val newId = ( cards.maxOfOrNull { it.id } ?: 0 ) + 1
        cards.add( Flashcard( newId, deckId, question, answer ))
    }
}