package com.cyberfeedforward.loyaltycardmanager2

import com.cyberfeedforward.loyaltycardmanager2.ui.home.HomeViewModel
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun initialState_hasNonBlankWelcomeMessage() {
        val viewModel = HomeViewModel()
        val message = viewModel.uiState.value.welcomeMessage

        assertTrue(message.isNotBlank())
        assertTrue(message.contains("LoyaltyCardManager"))
    }
}


