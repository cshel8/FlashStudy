package com.example.flashstudy.data.models

data class Flashcard(
    val id: Int,
    val deckId: Int,
    val question: String,
    val answer: String
)