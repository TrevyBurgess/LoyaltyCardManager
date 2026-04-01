package com.cyberfeedforward.mycardmanager.ui.screens.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun `increment increases counter`() {
        val viewModel = HomeViewModel()

        viewModel.onEvent(HomeEvent.OnIncrementClicked)

        assertEquals(1, viewModel.uiState.value.counter)
    }

    @Test
    fun `decrement at zero stays at zero (edge case)`() {
        val viewModel = HomeViewModel()

        viewModel.onEvent(HomeEvent.OnDecrementClicked)

        assertEquals(0, viewModel.uiState.value.counter)
    }
}
