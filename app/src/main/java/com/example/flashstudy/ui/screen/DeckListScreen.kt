package com.example.flashstudy.ui.screen

import android.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.flashstudy.ui.components.LimitCounter
import com.example.flashstudy.ui.components.LimitDialog

@Composable
fun DeckListScreen(
    repository: DeckRepository,
    onDeckClick: (Int) -> Unit,
    onAddDeck: () -> Unit
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
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align( Alignment.Center ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("FlashStudy")
                LimitCounter(
                    current = decks.size,
                    limit = 10,
                    label = "Decks"
                )
            }
            IconButton(
                onClick = {
                    if( deckLimitReached ) {
                        showLimitError = true
                    } else {
                        showAddDeckDialog = true
                        newDeckName = ""
                    }
                },
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( vertical = 6.dp )
                            .clickable {
                                onDeckClick( deck.id )
                            },
                        elevation = CardDefaults.cardElevation( defaultElevation = 4.dp )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding( 16.dp ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = deck.name,
                                modifier = Modifier.weight(1f)
                            )
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