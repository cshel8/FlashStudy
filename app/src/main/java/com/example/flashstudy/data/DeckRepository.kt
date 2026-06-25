package com.example.flashstudy.data

import androidx.compose.runtime.mutableStateListOf

data class Deck(
    val id: Int,
    val name: String
)
interface DeckRepository {
    fun getDecks(): List<Deck>
    fun getDeckById( id: Int ): Deck?
    fun addDeck( name: String )
    fun deleteDeck( deckId: Int )
}

class FakeDeckRepository : DeckRepository {
    private val decks = mutableStateListOf(
        Deck(1, "Spanish"),
        Deck(2, "History"),
        Deck(3, "Science")
    )
    override fun getDecks() = decks
    override fun getDeckById( id: Int): Deck? {
        return decks.firstOrNull { it.id == id }
    }
    override fun addDeck( name: String ) {
        val newId = ( decks.maxOfOrNull { it.id } ?: 0 ) + 1
        decks.add( Deck( newId, name ))
    }

    override fun deleteDeck(deckId: Int) {
        decks.removeAll { it.id == deckId }
    }
}