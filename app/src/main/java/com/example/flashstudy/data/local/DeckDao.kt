package com.example.flashstudy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Query( "SELECT * FROM decks" )
    fun getDecks(): Flow<List<DeckEntity>>

    @Query( "SELECT * FROM decks WHERE id = :id" )
    fun getDeckById( id: Int ): Flow<DeckEntity?>

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertDeck( deck: DeckEntity )

    @Query( "UPDATE decks SET name = :newName WHERE id = :deckId" )
    suspend fun updateDeck( deckId: Int, newName: String)

    @Query( "DELETE FROM decks WHERE id = :deckId" )
    suspend fun deleteDeck( deckId: Int )

    @Query( "SELECT COUNT( * ) FROM decks" )
    suspend fun getDeckCount(): Int
}