package com.example.flashstudy.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.flashstudy.data.models.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface FlashcardRepository {
    fun getCardsForDeck( deckId: Int ): Flow<List<Flashcard>>
    suspend fun addCard(
        deckId: Int,
        question: String,
        answer: String
    ): Boolean
    suspend fun updateCard(
        cardId: Int,
        question: String,
        answer: String
    )
    suspend fun deleteCard( cardId: Int )
}


class FakeFlashcardRepository : FlashcardRepository {
    private val cards = MutableStateFlow(
        listOf(
            Flashcard( 1, 1, "Hola", "Hello" ),
            Flashcard( 2, 2, "WW2 ended?", "1945" ),
            Flashcard( 3, 3, "What planet is known as the Red Planet?", "Mars" )
        )
    )
    override fun getCardsForDeck( deckId: Int ): Flow<List<Flashcard>> {
        return cards.map { list ->
            list.filter { card ->
                card.deckId == deckId
            }
        }
    }
    override suspend fun addCard( deckId: Int, question: String, answer: String): Boolean {
        val currentCards = cards.value
        if ( currentCards.count { it.deckId == deckId } >= 25 ) {
            return false
        }
        val newId = ( currentCards.maxOfOrNull { it.id } ?: 0 ) + 1
        cards.value = currentCards + Flashcard(
            id = newId,
            deckId = deckId,
            question = question,
            answer = answer
            )
        return true
    }
    override suspend fun updateCard( cardId: Int, question: String, answer: String ) {
        cards.value = cards.value.map { card ->
            if ( card.id == cardId ) {
                card.copy(
                    question = question,
                    answer = answer
                )
            } else {
                card
            }
        }
    }
    override suspend fun deleteCard( cardId: Int ) {
        cards.value = cards.value.filterNot { card ->
            card.id == cardId
        }
    }
}