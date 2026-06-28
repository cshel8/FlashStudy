package com.example.flashstudy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query( "SELECT * FROM flashcards WHERE deckId = :deckId" )
    fun getCardsForDeck( deckId: Int ): Flow<List<FlashcardEntity>>

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertCard( card: FlashcardEntity )

    @Query("""
        UPDATE flashcards
        SET question = :question, answer = :answer
        WHERE id = :cardId
    """)
    suspend fun updateCard( cardId: Int, question: String, answer: String)

    @Query( "DELETE FROM flashcards WHERE id = :cardId" )
    suspend fun deleteCard( cardId: Int )

    @Query( "SELECT COUNT( * ) FROM flashcards WHERE deckId = :deckId" )
    suspend fun getCardCountForDeck( deckId: Int ): Int
}