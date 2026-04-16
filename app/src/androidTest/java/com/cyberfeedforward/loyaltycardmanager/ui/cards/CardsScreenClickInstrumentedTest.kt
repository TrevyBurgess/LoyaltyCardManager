package com.cyberfeedforward.loyaltycardmanager.ui.cards

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class CardsScreenClickInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun clickingGridItem_triggersCardClickCallback() {
        composeRule.setContent {
            var clickedIndex by mutableStateOf<Int?>(null)

            CardsScreen(
                uiState = CardsUiState(
                    cardCount = 0,
                    isScannerVisible = false,
                    scanResult = null,
                ),
                savedScans = listOf(
                    ScanHistoryStorage.SavedScan(
                        name = "Costco",
                        code = "1234567890",
                        type = ScannedCodeType.Barcode1D,
                    ),
                ),
                onAddCard = {},
                onRemoveCard = {},
                onScan = {},
                onEditScan = {},
                onDeleteScan = {},
                onCardClick = { clickedIndex = it },
            )

            if (clickedIndex != null) {
                Text(text = "Clicked")
            }
        }

        composeRule.onNodeWithText("Costco").performClick()
        composeRule.onNodeWithText("Clicked").assertIsDisplayed()
    }
}


