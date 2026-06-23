package com.example.flashstudy.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashstudy.data.FakeFlashcardRepository

@Composable
fun StudyScreen(
    deckId: Int,
    flashcardRepository: FakeFlashcardRepository,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val cards = flashcardRepository.getCardsForDeck( deckId )
    if ( cards.isEmpty() ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text( "No flashcards in this deck" )
        }
        return
    }
    var index by remember { mutableStateOf( 0 ) }
    var isFlipped by remember { mutableStateOf( false )}
    LaunchedEffect( deckId ) {
        index = 0
        isFlipped = false
    }
    if ( index > cards.lastIndex ) {
        index = 0
    }
    val card = cards[ index ]
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding( 16.dp )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.align( Alignment.CenterStart )
            ) {
            Text( "Back" )
            }
            Text(
                text = "Study Screen",
                modifier = Modifier.align( Alignment.Center )
            )
            Button(
                onClick = onHome,
                modifier = Modifier.align( Alignment.CenterEnd )
            ) {
                Text("Home")
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column( horizontalAlignment = Alignment.CenterHorizontally ) {
                Text("Card ${index + 1} of ${cards.size}")
                Text(
                    if ( isFlipped ) card.answer else card.question
                )
                Button(
                    onClick = { isFlipped = !isFlipped }
                ) {
                    Text( "Flip Card" )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Row {
                Button(
                    onClick = {
                        if ( index > 0 ) {
                            index--
                            isFlipped = false
                        }
                    }
                ) {
                    Text( "Previous" )
                }
                Button(
                    onClick = {
                        if ( index < cards.lastIndex ) {
                            index++
                            isFlipped = false
                        }
                    }
                ) {
                    Text( "Next" )
                }
            }
        }
    }
}