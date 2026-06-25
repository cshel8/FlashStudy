package com.example.flashstudy.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.unit.dp
import com.example.flashstudy.data.DeckRepository
import com.example.flashstudy.data.FakeDeckRepository
import com.example.flashstudy.data.FakeFlashcardRepository
import com.example.flashstudy.ui.components.ConfirmDeleteDialog

@Composable
fun DeckScreen(
    deckId: Int,
    repository: DeckRepository,
    flashcardRepository: FakeFlashcardRepository,
    onStartStudy: () -> Unit,
    onBack: () -> Unit
) {
    val deck = repository.getDeckById(deckId)
    val cards = flashcardRepository.getCardsForDeck( deckId )
    var showAddCardDialog by remember { mutableStateOf( false )}
    var newQuestion by remember { mutableStateOf( "")}
    var newAnswer by remember { mutableStateOf( "" )}
    var editingCardId by remember { mutableStateOf<Int?>( null )}
    var editQuestion by remember { mutableStateOf( "" )}
    var editAnswer by remember { mutableStateOf( "" )}
    var deletingCardId by remember { mutableStateOf<Int?>( null )}
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
                    showAddCardDialog = true
                },
                modifier = Modifier.align( Alignment.CenterEnd )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Flashcard"
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( top = 8.dp )
            ) {
                items( cards ) { card ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( vertical = 6.dp )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding( 16.dp ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column( modifier = Modifier.weight( 1f )) {
                                    Text( text = card.question )
                                    Text( text = card.answer )
                                }
                                Row {
                                    IconButton(
                                        onClick = {
                                            editingCardId = card.id
                                            editQuestion = card.question
                                            editAnswer = card.answer
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Card"
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            deletingCardId = card.id
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Card"
                                        )
                                    }
                                }
                            }
                        }
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
    if ( showAddCardDialog ) {
        AlertDialog(
            onDismissRequest = {
                showAddCardDialog = false
            },
            title = {
                Text( "New Flashcard" )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy( 8.dp )
                ) {
                    TextField(
                        value = newQuestion,
                        onValueChange = { newQuestion = it },
                        label = { Text( "Question" )}
                    )
                    TextField(
                        value = newAnswer,
                        onValueChange = { newAnswer = it },
                        label = { Text( "Answer" )}
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    flashcardRepository.addCard(
                        deckId,
                        newQuestion,
                        newAnswer
                    )
                    newQuestion = ""
                    newAnswer = ""
                    showAddCardDialog = false
                }) {
                    Text( "Create" )
                }
            },
            dismissButton = {
                Button(onClick = {
                    showAddCardDialog = false
                }) {
                    Text( "Cancel" )
                }
            }
        )
    }
    if ( editingCardId != null ) {
        AlertDialog(
            onDismissRequest = {
                editingCardId = null
            },
            title = {
                Text( "Edit Flashcard")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy( 8.dp )
                ) {
                    TextField(
                        value = editQuestion,
                        onValueChange = { editQuestion = it },
                        label = { Text( "Question" )}
                    )
                    TextField(
                        value = editAnswer,
                        onValueChange = { editAnswer = it },
                        label = { Text( "Answer" )}
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        flashcardRepository.updateCard(
                            editingCardId!!,
                            editQuestion,
                            editAnswer
                        )
                        editingCardId = null
                    }
                ) {
                    Text( "Save" )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        editingCardId = null
                    }
                ) {
                    Text( "Cancel" )
                }
            }
        )
    }
    if (deletingCardId != null ) {
        ConfirmDeleteDialog(
            title = "Delete Flashcard",
            message = "Are you sure you want to delete this flashcard?",
            onDismiss = {
                deletingCardId = null
            },
            onConfirm = {
                flashcardRepository.deleteCard( deletingCardId!! )
                deletingCardId = null
            }
        )
    }
}