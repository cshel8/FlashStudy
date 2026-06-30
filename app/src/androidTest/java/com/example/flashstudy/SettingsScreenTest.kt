package com.example.flashstudy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.flashstudy.ui.screen.SettingsScreen
import com.example.flashstudy.ui.theme.FlashStudyTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_displaysAccessibilitySection() {
        composeTestRule.setContent {
            FlashStudyTheme {
                SettingsScreen(
                    shuffleCards = false,
                    darkTheme = false,
                    colorblindAssist = false,
                    onShuffleChange = {},
                    onDarkThemeChange = {},
                    onColorblindAssistChange = {},
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Accessibility")
            .assertIsDisplayed()
    }
}