package com.example.flashstudy.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.unit.dp
import com.example.flashstudy.data.DeckRepository
import com.example.flashstudy.data.FakeDeckRepository
import com.example.flashstudy.data.FakeFlashcardRepository

@Composable
fun DeckScreen(
    deckId: Int,
    repository: DeckRepository,
    flashcardRepository: FakeFlashcardRepository,
    onStartStudy: () -> Unit,
    onBack: () -> Unit
) {
    val deck = repository.getDeckById(deckId)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("Back")
            }
            if (deck != null) {
                Text(
                    text = deck.name,
                    modifier = Modifier.align(Alignment.Center))
            } else {
                Text(
                    text = "Deck not found",
                    modifier = Modifier.align(Alignment.Center))
            }
            IconButton(
                onClick = {
                    flashcardRepository.addCard(
                        deckId,
                        "New Question",
                        "New Answer"
                    )
                },
                modifier = Modifier.align( Alignment.CenterEnd )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Flashcard"
                )
            }
        }
        val cards = flashcardRepository.getCardsForDeck( deckId )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.align( Alignment.TopCenter )
            ) {
                cards.forEach { card ->
                    Text( "${card.question} -> ${card.answer}" )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onStartStudy) {
                Text("Start Study")
            }
        }
    }
}