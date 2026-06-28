package com.example.flashstudy.data.repository

import com.example.flashstudy.data.models.Deck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface DeckRepository {
    fun getDecks(): Flow<List<Deck>>
    fun getDeckById( id: Int ): Flow<Deck?>
    suspend fun addDeck( name: String ): Boolean
    suspend fun updateDeck( deckId: Int, newName: String )
    suspend fun deleteDeck( deckId: Int )
}

class FakeDeckRepository : DeckRepository {
    private val MAX_DECKS = 10
    private val decks = MutableStateFlow(
        listOf(
            Deck(1, "Spanish"),
            Deck(2, "History"),
            Deck(3, "Science")
        )
    )
    override fun getDecks(): Flow<List<Deck>> {
        return decks
    }
    override fun getDeckById( id: Int): Flow<Deck?> {
        return decks.map { list ->
            list.firstOrNull { deck ->
                deck.id == id
            }
        }
    }
    override suspend fun addDeck( name: String ): Boolean {
        val currentDecks = decks.value
        if ( currentDecks.size >= MAX_DECKS ) {
            return false
        }
        val newId = ( currentDecks.maxOfOrNull { it.id } ?: 0 ) + 1
        decks.value = currentDecks + Deck(
            id = newId,
            name = name
        )
        return true
    }
    override suspend fun updateDeck( deckId: Int, newName: String ) {
        decks.value = decks.value.map { deck ->
            if ( deck.id == deckId ) {
                deck.copy(name = newName)
            } else {
                deck
            }
        }
    }

    override suspend fun deleteDeck(deckId: Int) {
        decks.value = decks.value.filterNot { deck ->
            deck.id == deckId
        }
    }
}