package com.example.calculator

import com.example.calculator.data.viewmodel.CalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorViewModelTest {
    private val viewModel = CalculatorViewModel()

    @Test
    fun calculatorViewModel_spinningAction_verifyIsSpinningBackwardsChanged() {
        val initialValue = viewModel.isSpinningBackwards

        viewModel.spinningAction()

        val finalValue = viewModel.isSpinningBackwards
        assertEquals(initialValue, !finalValue)
    }

    @Test
    fun updateCurrentValue_verifyCurrentStringValueUpdated() {
        val initialValue = viewModel.uiState.value.currentStringValue

        viewModel.updateCurrentValue("5")

        val finalValue = viewModel.uiState.value.currentStringValue

        assertEquals(initialValue + "5", finalValue)
    }

    @Test
    fun clearValue_verifyCurrentStringValueCleared() {
        viewModel.clearValue()

        val finalValue = viewModel.uiState.value.currentStringValue

        assertEquals("", finalValue)
    }

    @Test
    fun backspaceAction_verifyCurrentStringValueUpdated() {
        viewModel.updateCurrentValue("54")

        viewModel.backspaceAction()

        val finalValue = viewModel.uiState.value.currentStringValue

        assertEquals("5", finalValue)
    }

    @Test
    fun getPercentage_verifyCurrentStringValueUpdated() {
        viewModel.updateCurrentValue("50")

        viewModel.getPercentage()

        val finalValue = viewModel.uiState.value.currentStringValue

        assertEquals("0.5", finalValue)
    }

    @Test
    fun equalsAction_emptyInput() {
        viewModel.updateCurrentValue("")

        viewModel.equalsAction()

        val result = viewModel.uiState.value.currentStringValue

        assertEquals("", result)
    }

    @Test
    fun equalsAction_errorHandling() {
        viewModel.updateCurrentValue("10 / 0")

        viewModel.equalsAction()

        val result = viewModel.uiState.value.currentStringValue

        assertEquals("Error", result)
    }

    @Test
    fun calculateResults_simpleAddition() {
        viewModel.updateCurrentValue("2 + 2")

        val result = viewModel.calculateResults()

        assertEquals("4", result)
    }

    @Test
    fun calculateResults_subtraction() {
        viewModel.updateCurrentValue("5 - 3")

        val result = viewModel.calculateResults()

        assertEquals("2", result)
    }

    @Test
    fun calculateResults_multiplication() {
        viewModel.updateCurrentValue("-6 * 4")

        val result = viewModel.calculateResults()

        assertEquals("-24", result)
    }

    @Test
    fun calculateResults_division() {
        viewModel.updateCurrentValue("10 / 2")

        val result = viewModel.calculateResults()

        assertEquals("5", result)
    }

    @Test
    fun calculateResults_decimalResult() {
        viewModel.updateCurrentValue("10.5 / 2")

        val result = viewModel.calculateResults()

        assertEquals("5.25", result)
    }
}