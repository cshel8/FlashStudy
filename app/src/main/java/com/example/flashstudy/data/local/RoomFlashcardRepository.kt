package com.example.flashstudy.data.local

import com.example.flashstudy.data.models.Flashcard
import com.example.flashstudy.data.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomFlashcardRepository(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {
    override fun getCardsForDeck( deckId: Int ): Flow<List<Flashcard>> {
        return flashcardDao.getCardsForDeck( deckId ).map { entities ->
            entities.map { entity ->
                Flashcard(
                    id = entity.id,
                    deckId = entity.deckId,
                    question = entity.question,
                    answer = entity.answer
                )
            }
        }
    }
    override suspend fun addCard(
        deckId: Int,
        question: String,
        answer: String
    ): Boolean {
        val count = flashcardDao.getCardCountForDeck( deckId )
        if ( count >= 25 ) {
            return false
        }
        flashcardDao.insertCard(
            FlashcardEntity(
                deckId = deckId,
                question = question,
                answer = answer
            )
        )
        return true
    }
    override suspend fun updateCard(
        cardId: Int,
        question: String,
        answer: String
    ) {
        flashcardDao.updateCard( cardId, question, answer )
    }
    override suspend fun deleteCard( cardId: Int ) {
        flashcardDao.deleteCard(cardId)
    }
}