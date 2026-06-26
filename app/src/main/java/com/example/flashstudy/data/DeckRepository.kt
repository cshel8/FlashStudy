package com.example.flashstudy.data

import androidx.compose.runtime.mutableStateListOf

data class Deck(
    val id: Int,
    val name: String
)
interface DeckRepository {
    fun getDecks(): List<Deck>
    fun getDeckById( id: Int ): Deck?
    fun addDeck( name: String ): Boolean
    fun updateDeck( deckId: Int, newName: String )
    fun deleteDeck( deckId: Int )
}

class FakeDeckRepository : DeckRepository {
    private val MAX_DECKS = 10
    private val decks = mutableStateListOf(
        Deck(1, "Spanish"),
        Deck(2, "History"),
        Deck(3, "Science")
    )
    override fun getDecks() = decks
    override fun getDeckById( id: Int): Deck? {
        return decks.firstOrNull { it.id == id }
    }
    override fun addDeck( name: String ): Boolean {
        if ( decks.size >= MAX_DECKS ) {
            return false
        }
        val newId = ( decks.maxOfOrNull { it.id } ?: 0 ) + 1
        decks.add( Deck( newId, name ))
        return true
    }
    override fun updateDeck( deckId: Int, newName: String ) {
        val index = decks.indexOfFirst { it.id == deckId }
        if ( index != -1 ) {
            decks[ index ] = decks[ index ].copy( name = newName )
        }
    }

    override fun deleteDeck(deckId: Int) {
        decks.removeAll { it.id == deckId }
    }
}