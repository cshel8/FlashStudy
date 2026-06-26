package com.example.flashstudy.ui.model

enum class CardSide {
    QUESTION,
    ANSWER;

    fun flip(): CardSide {
        return if ( this == QUESTION ) ANSWER else QUESTION
    }
}