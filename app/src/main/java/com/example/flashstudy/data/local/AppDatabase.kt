package com.example.flashstudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DeckEntity::class,
        FlashcardEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun flashcardDao(): FlashcardDao
}