package com.example.flashstudy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "flashcards" )
data class FlashcardEntity(
    @PrimaryKey( autoGenerate = true )
    val id: Int = 0,
    val deckId: Int,
    val question: String = "",
    val answer: String = ""
)