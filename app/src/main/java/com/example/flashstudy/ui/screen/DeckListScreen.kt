package com.example.flashstudy.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.flashstudy.ui.components.ConfirmDeleteDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashstudy.data.DeckRepository

@Composable
fun DeckListScreen(
    repository: DeckRepository,
    onDeckClick: (Int) -> Unit,
    onAddDeck: () -> Unit
) {
    val decks = repository.getDecks()
    var deletingDeckId by remember { mutableStateOf<Int?>( null ) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "FlashStudy",
                modifier = Modifier.align( Alignment.Center )
            )
            IconButton(
                onClick = onAddDeck,
                modifier = Modifier.align( Alignment.CenterEnd )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Deck"
                )
            }
        }
        Column(
            modifier = Modifier.padding( top = 16.dp )
        ) {
            decks.forEach { deck ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDeckClick( deck.id ) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(deck.name)
                    }
                    IconButton(
                        onClick = {
                            deletingDeckId = deck.id
                        },
                        modifier = Modifier.align( Alignment.CenterEnd )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Deck"
                        )
                    }
                }
            }
        }
    }
    if (deletingDeckId != null ) {
        ConfirmDeleteDialog(
            title = "Delete Deck",
            message = "Are you sure you want to delete this deck?",
            onDismiss = {
                deletingDeckId = null
            },
            onConfirm = {
                repository.deleteDeck( deletingDeckId!! )
                deletingDeckId = null
            }
        )
    }
}