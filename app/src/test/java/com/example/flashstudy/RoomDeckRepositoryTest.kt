package com.example.flashstudy

import com.example.flashstudy.data.local.DeckDao
import com.example.flashstudy.data.local.DeckEntity
import com.example.flashstudy.data.local.RoomDeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomDeckRepositoryTest {
    @Test
    fun addDeck_returnsFalse_whenDeckLimitReached() = runTest {
        val fakeDao = FakeDeckDao()
        val repository = RoomDeckRepository( fakeDao)

        for ( i in 1..10 ) {
            assertTrue( repository.addDeck( "Deck $i" ) )
        }
        val result = repository.addDeck( "Deck 11" )
        assertFalse( result )

        val decks = repository.getDecks().first()
        assertEquals( 10, decks.size )
    }
}
class FakeDeckDao : DeckDao {
    private val decks = MutableStateFlow<List<DeckEntity>>( emptyList() )
    override fun getDecks(): Flow<List<DeckEntity>> {
        return decks
    }

    override fun getDeckById(id: Int): Flow<DeckEntity?> {
        return MutableStateFlow(
            decks.value.firstOrNull { it.id == id }
        )
    }
    override suspend fun insertDeck( deck: DeckEntity ) {
        val newId = ( decks.value.maxOfOrNull { it.id } ?: 0 ) + 1
        decks.value = decks.value + deck.copy( id = newId )
    }

    override suspend fun updateDeck(deckId: Int, newName: String) {
        decks.value = decks.value.map { deck ->
            if ( deck.id == deckId ) {
                deck.copy( name = newName )
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

    override suspend fun getDeckCount(): Int {
        return decks.value.size
    }
}