package com.example.flashstudy.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.flashstudy.data.repository.DeckRepository
import com.example.flashstudy.data.repository.FlashcardRepository
import com.example.flashstudy.ui.components.ConfirmDeleteDialog
import com.example.flashstudy.ui.components.LimitCounter
import com.example.flashstudy.ui.components.LimitDialog

@Composable
fun DeckScreen(
    deckId: Int,
    repository: DeckRepository,
    flashcardRepository: FlashcardRepository,
    onStartStudy: () -> Unit,
    onBack: () -> Unit
) {
    val deck by repository
        .getDeckById( deckId )
        .collectAsState( initial = null )
    val cards by flashcardRepository
        .getCardsForDeck( deckId )
        .collectAsState( initial = emptyList() )
    val scope = rememberCoroutineScope()
    val cardLimitReached = cards.size >= 25
    var showAddCardDialog by remember { mutableStateOf( false )}
    var newQuestion by remember { mutableStateOf( "")}
    var newAnswer by remember { mutableStateOf( "" )}
    var editingCardId by remember { mutableStateOf<Int?>( null )}
    var editQuestion by remember { mutableStateOf( "" )}
    var editAnswer by remember { mutableStateOf( "" )}
    var deletingCardId by remember { mutableStateOf<Int?>( null )}
    var showLimitError by remember { mutableStateOf( false ) }
    var showValidationError by remember { mutableStateOf( false ) }
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
            Column(
                modifier = Modifier.align( Alignment.Center ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val currentDeck = deck
                if (currentDeck != null) {
                    Text(
                        text = currentDeck.name
                    )
                    LimitCounter(
                        current = cards.size,
                        limit = 25,
                        label = "Flashcards"
                    )
                } else {
                    Text("Deck not found")
                }
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
                                    FilledTonalIconButton(
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
                                    FilledIconButton(
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
        Spacer( modifier = Modifier.height( 16.dp ) )
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if ( cardLimitReached ) {
                        showLimitError = true
                    } else {
                        newQuestion = ""
                        newAnswer = ""
                        showAddCardDialog = true
                    }
                },
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( vertical = 28.dp ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy( 8.dp ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Flashcard"
                    )
                    Text(
                        "Create New Flashcard",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer( Modifier.height( 8.dp ) )
                Text(
                    text = "You can have up to 25 flashcards",
                    style = MaterialTheme.typography.bodySmall
                )
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
                        onValueChange = {
                            newQuestion = it
                            showValidationError = false
                        },
                        label = { Text( "Question" ) },
                        isError = showValidationError && newQuestion.isBlank(),
                        supportingText = {
                            if ( showValidationError && newQuestion.isBlank()) {
                                Text( "Question is required" )
                            }
                        }
                    )
                    TextField(
                        value = newAnswer,
                        onValueChange = {
                            newAnswer = it
                            showValidationError = false
                        },
                        label = { Text( "Answer" ) },
                        isError = showValidationError && newAnswer.isBlank(),
                        supportingText = {
                            if ( showValidationError && newAnswer.isBlank() ) {
                                Text( "Answer is required" )
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newQuestion.trim().isEmpty() || newAnswer.trim().isEmpty()) {
                            showValidationError = true
                            return@Button
                        }
                        scope.launch {
                            val success = flashcardRepository.addCard(
                                deckId,
                                newQuestion,
                                newAnswer
                            )
                            if (!success) {
                                showLimitError = true
                            }
                            newQuestion = ""
                            newAnswer = ""
                            showAddCardDialog = false
                        }
                    }
                ) {
                    Text( "Create" )
                }
            },
            dismissButton = {
                Button(onClick = {
                    newQuestion = ""
                    newAnswer = ""
                    showValidationError = false
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
                        scope.launch {
                            flashcardRepository.updateCard(
                                editingCardId!!,
                                editQuestion,
                                editAnswer
                            )
                            editingCardId = null
                        }
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
                scope.launch {
                    flashcardRepository.deleteCard(deletingCardId!!)
                    deletingCardId = null
                }
            }
        )
    }
    if ( showLimitError ) {
        LimitDialog(
            title = "Flashcard limit reached",
            message = "You can only have up to 25 flashcards in a deck.",
            onDismiss = {
                showLimitError = false
            }
        )
    }
}