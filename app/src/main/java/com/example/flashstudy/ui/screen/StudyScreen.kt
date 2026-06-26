package com.example.flashstudy.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flashstudy.data.FakeFlashcardRepository

@Composable
fun StudyScreen(
    deckId: Int,
    deckName: String,
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
    val rotation by animateFloatAsState(
        targetValue = if ( isFlipped ) 180f else 0f,
        animationSpec = tween( durationMillis = 400 ),          //normal
//        animationSpec = spring(                               //bouncy
//            dampingRatio = 0.5f,
//            stiffness = 200f
//        ),
        label = "cardFlip"
    )
    LaunchedEffect( deckId ) {
        index = 0
        isFlipped = false
    }
    val safeIndex = index.coerceIn( 0, cards.lastIndex )
    val card = cards[ safeIndex ]
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
            Column(
                modifier = Modifier.align( Alignment.Center ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = deckName,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Study Mode",
                    style = MaterialTheme.typography.bodySmall
                )
            }
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
                Card(
                    modifier = Modifier
                        .padding( 16.dp )
                        .fillMaxWidth()
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 8 * density
                        }
                        .clickable { isFlipped = !isFlipped },
                    elevation = CardDefaults.cardElevation( defaultElevation = 8.dp ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .padding( 48.dp )
                            .height( 200.dp )
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if ( isFlipped ) card.answer else card.question,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.graphicsLayer {
                                rotationY = if ( isFlipped ) 180f else 0f
                            }
                        )
                    }
                }
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
            Row(
                horizontalArrangement = Arrangement.spacedBy( 16.dp )
            ) {
                Button(
                    onClick = {
                        index = if ( index > 0 ) index - 1 else cards.lastIndex
                        isFlipped = false
                        }
                ) {
                    Text( "Previous" )
                }
                Button(
                    onClick = {
                        index = if ( index < cards.lastIndex ) index + 1 else 0
                        isFlipped = false
                    }
                ) {
                    Text( "Next" )
                }
            }
        }
    }
}