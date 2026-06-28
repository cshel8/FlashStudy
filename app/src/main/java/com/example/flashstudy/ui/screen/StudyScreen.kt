package com.example.flashstudy.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import com.example.flashstudy.data.repository.FlashcardRepository
import com.example.flashstudy.ui.model.CardSide

@Composable
fun StudyScreen(
    deckId: Int,
    deckName: String,
    flashcardRepository: FlashcardRepository,
    shuffleCards: Boolean,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val cards by flashcardRepository
        .getCardsForDeck( deckId )
        .collectAsState( initial = emptyList() )
    var index by remember { mutableStateOf( 0 ) }
    var currentSide by remember {
        mutableStateOf( CardSide.QUESTION)
    }
    var slideDirection by remember { mutableStateOf( 1 ) }
    var pendingIndex by remember { mutableStateOf<Int?>( null )}
    var studyCards by remember { mutableStateOf( cards ) }
    LaunchedEffect( cards, shuffleCards, deckId ) {
        studyCards =
            if ( shuffleCards ) {
                cards.shuffled()
            } else {
                cards
            }
        index = 0
        currentSide = CardSide.QUESTION
        pendingIndex = null
    }
    val hasCards = studyCards.isNotEmpty()
    val rotation by animateFloatAsState(
        targetValue =
            if ( currentSide == CardSide.ANSWER ) 180f
            else 0f,
        animationSpec = tween( durationMillis = 400 ),
        label = "cardFlip"
    )
    LaunchedEffect( deckId ) {
        index = 0
        currentSide = CardSide.QUESTION
    }
    LaunchedEffect( rotation ){
        if (
            currentSide == CardSide.QUESTION &&
            rotation == 0f &&
            pendingIndex != null
        ) {
            index = pendingIndex!!
            pendingIndex = null
        }
    }
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
            if ( !hasCards ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No flashcards in this deck")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Card ${index + 1} of ${studyCards.size}")
                    AnimatedContent(
                        targetState = index,
                        transitionSpec = {
                            if ( slideDirection > 0 ) {
                                slideInHorizontally { it } togetherWith
                                slideOutHorizontally { -it }
                            } else {
                                slideInHorizontally { -it } togetherWith
                                slideOutHorizontally { it }
                            }
                        },
                        label = "cardTransition"
                    ) { currentIndex ->
                        val currentCard = studyCards[ currentIndex ]
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 8 * density
                                }
                                .clickable {
                                    currentSide = currentSide.flip()
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(48.dp)
                                    .height(200.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                val visibleSide =
                                    if ( rotation <= 90f ) CardSide.QUESTION else CardSide.ANSWER
                                Text(
                                    text = when ( visibleSide ){
                                        CardSide.QUESTION -> currentCard.question
                                        CardSide.ANSWER -> currentCard.answer
                                    },
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.graphicsLayer {
                                        rotationY =
                                            if ( visibleSide == CardSide.ANSWER )
                                            180f
                                        else
                                            0f
                                    }
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            currentSide = currentSide.flip()
                        }
                    ) {
                        Text("Flip Card")
                    }
                }
            }
        }
        if ( hasCards ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy( 16.dp )
                ) {
                    Button(
                        onClick = {
                            slideDirection = -1
                            val newIndex =
                                if ( index > 0 ) index - 1 else studyCards.lastIndex
                            if ( currentSide == CardSide.ANSWER ) {
                                currentSide = CardSide.QUESTION
                                pendingIndex = newIndex
                            } else {
                                index = newIndex
                            }
                        }
                    ) {
                        Text("Previous")
                    }
                    Button(
                        onClick = {
                            slideDirection = 1
                            val newIndex =
                                if ( index < studyCards.lastIndex ) index + 1 else 0
                            if ( currentSide == CardSide.ANSWER ) {
                                currentSide = CardSide.QUESTION
                                pendingIndex = newIndex
                            } else {
                                index = newIndex
                            }
                        }
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}