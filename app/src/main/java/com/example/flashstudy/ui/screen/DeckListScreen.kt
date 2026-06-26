package com.example.flashstudy.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.flashstudy.ui.components.ConfirmDeleteDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashstudy.data.DeckRepository
import com.example.flashstudy.data.FakeFlashcardRepository
import com.example.flashstudy.ui.components.LimitCounter
import com.example.flashstudy.ui.components.LimitDialog

@Composable
fun DeckListScreen(
    repository: DeckRepository,
    flashcardRepository: FakeFlashcardRepository,
    onDeckClick: (Int) -> Unit,
    onAddDeck: () -> Unit,
    onStudyClick: (Int) -> Unit,
    onSettingsClick: () -> Unit
) {
    val decks = repository.getDecks()
    val deckLimitReached = decks.size >= 10
    var deletingDeckId by remember { mutableStateOf<Int?>( null ) }
    var editingDeckId by remember { mutableStateOf<Int?>( null ) }
    var editDeckName by remember { mutableStateOf( "" ) }
    var showAddDeckDialog by remember { mutableStateOf( false ) }
    var newDeckName by remember { mutableStateOf( "" ) }
    var showLimitError by remember { mutableStateOf( false ) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "FlashStudy",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.Center )
                )
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.align( Alignment.CenterEnd )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( top = 24.dp ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Decks",
                    modifier = Modifier.padding( end = 16.dp )
                )
                Column {
                    Text(
                        text = "My Decks",
                        style = MaterialTheme.typography.titleLarge
                    )
                    LimitCounter(
                        current = decks.size,
                        limit = 10,
                        label = "Decks"
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding( top = 16.dp )
        ) {
            decks.forEach { deck ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val cardCount = flashcardRepository.getCardsForDeck(deck.id).size
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = deck.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "$cardCount flashcards",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Row(
                                    modifier = Modifier.padding( top = 8.dp ),
                                    horizontalArrangement = Arrangement.spacedBy( 8.dp )
                                ) {
                                    Button(
                                        onClick = {
                                            onDeckClick( deck.id )
                                        }
                                    ) {
                                        Text( "Manage" )
                                    }
                                    Button(
                                        onClick = {
                                            onStudyClick(deck.id)
                                    }
                                ) {
                                    Text("Study")
                                }
                            }
                        }
                        Row {
                            IconButton(
                                    onClick = {
                                        editingDeckId = deck.id
                                        editDeckName = deck.name
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Deck"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        deletingDeckId = deck.id
                                    }
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
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (deckLimitReached) {
                            showLimitError = true
                        } else {
                            showAddDeckDialog = true
                            newDeckName = ""
                        }
                    },
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( vertical = 28.dp, horizontal = 24.dp ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy( 8.dp )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create New Deck"
                        )
                        Text(
                            text = "Create New Deck",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer( modifier = Modifier.height( 8.dp ) )
                    Text(
                        text = "You can have up to 10 decks",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
    if ( showAddDeckDialog ) {
        AlertDialog(
            onDismissRequest = {
                showAddDeckDialog = false
            },
            title = {
                Text( "New Deck" )
            },
            text = {
                TextField(
                    value = newDeckName,
                    onValueChange = { newDeckName = it },
                    label = { Text( "Deck Name" ) }
                )
            },
            confirmButton = {
                Button( onClick = {
                    if ( newDeckName.isNotBlank() ) {
                        val success = repository.addDeck( newDeckName )
                        if ( !success ) {
                            showLimitError = true
                        }
                    }
                    newDeckName = ""
                    showAddDeckDialog = false
                } ) {
                    Text( "Create" )
                }
            },
            dismissButton = {
                Button( onClick = {
                    showAddDeckDialog = false
                    newDeckName = ""
                }) {
                    Text( "Cancel" )
                }
            }
        )
    }
    if ( editingDeckId != null ) {
        AlertDialog(
            onDismissRequest = {
                editingDeckId = null
            },
            title = {
                Text( "Edit Deck" )
            },
            text = {
                Column {
                    TextField(
                        value = editDeckName,
                        onValueChange = { editDeckName = it },
                        label = { Text( "Deck Name" ) }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.updateDeck( editingDeckId!!, editDeckName )
                        editingDeckId = null
                    }
                ) {
                    Text( "Save" )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        editingDeckId = null
                    }
                ) {
                    Text( "Cancel" )
                }
            }
        )
    }
    if ( deletingDeckId != null ) {
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
    if ( showLimitError ) {
        LimitDialog(
            title = "Deck limit reached",
            message = "You can only have up to 10 decks.",
            onDismiss = { showLimitError = false }
        )
    }
}