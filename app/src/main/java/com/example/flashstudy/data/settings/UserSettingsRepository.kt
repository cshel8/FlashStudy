package com.example.flashstudy.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user-settings"
)
class UserSettingsRepository(
    private val context: Context
) {
    private val shuffleCardsKey = booleanPreferencesKey( "shuffle_cards" )
    val shuffleCards: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[ shuffleCardsKey ] ?: false
        }
    suspend fun setShuffleCards( enabled: Boolean ) {
        context.dataStore.edit { preferences ->
            preferences[ shuffleCardsKey ] = enabled
        }
    }
}