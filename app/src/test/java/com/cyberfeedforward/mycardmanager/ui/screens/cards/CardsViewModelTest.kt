package com.cyberfeedforward.mycardmanager.ui.screens.cards

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CardsViewModelTest {

    @Test
    fun `add card with valid name appends card and clears input`() {
        val viewModel = CardsViewModel()

        viewModel.onEvent(CardsEvent.OnNewCardNameChanged("Pikachu"))
        viewModel.onEvent(CardsEvent.OnAddCardClicked)

        assertEquals(listOf("Pikachu"), viewModel.uiState.value.cards)
        assertEquals("", viewModel.uiState.value.newCardName)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `add card with blank name sets error (negative case)`() {
        val viewModel = CardsViewModel()

        viewModel.onEvent(CardsEvent.OnNewCardNameChanged("   "))
        viewModel.onEvent(CardsEvent.OnAddCardClicked)

        assertEquals(emptyList<String>(), viewModel.uiState.value.cards)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `remove card with invalid index sets error (edge case)`() {
        val viewModel = CardsViewModel()

        viewModel.onEvent(CardsEvent.OnNewCardNameChanged("A"))
        viewModel.onEvent(CardsEvent.OnAddCardClicked)
        viewModel.onEvent(CardsEvent.OnRemoveCardClicked(index = 99))

        assertEquals(listOf("A"), viewModel.uiState.value.cards)
        assertEquals("Invalid card index", viewModel.uiState.value.errorMessage)
    }
}
