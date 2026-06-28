package com.example.flashstudy.data.local

import com.example.flashstudy.data.models.Deck
import com.example.flashstudy.data.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDeckRepository(
    private val deckDao: DeckDao
) : DeckRepository {
    override fun getDecks(): Flow<List<Deck>> {
        return deckDao.getDecks().map { entities ->
            entities.map { entity ->
                Deck(
                    id = entity.id,
                    name = entity.name
                )
            }
        }
    }
    override fun getDeckById( id: Int ): Flow<Deck?> {
        return deckDao.getDeckById( id ).map { entity ->
            entity?.let {
                Deck(
                    id = it.id,
                    name = it.name
                )
            }
        }
    }

    override suspend fun addDeck( name: String ): Boolean {
        val count = deckDao.getDeckCount()
        if ( count >= 10 ){
            return false
        }
        deckDao.insertDeck(
            DeckEntity( name = name )
        )
        return true
    }

    override suspend fun updateDeck( deckId: Int, newName: String ) {
        deckDao.updateDeck( deckId, newName)
    }

    override suspend fun deleteDeck( deckId: Int ) {
        deckDao.deleteDeck( deckId )
    }
}