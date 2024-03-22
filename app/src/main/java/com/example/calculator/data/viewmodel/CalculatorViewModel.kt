package com.example.calculator.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CalculatorUiState()
    )
    val uiState: StateFlow<CalculatorUiState> = _uiState

    var isSpinningBackwards by mutableStateOf(false)

    fun spinningAction() {
        isSpinningBackwards = !isSpinningBackwards
    }

    fun updateCurrentValue(digit: String) {
        _uiState.update {
            it.copy(currentStringValue = it.currentStringValue + digit)
        }
    }

    fun clearValue() = _uiState.update { it.copy(currentStringValue = "") }

    fun backspaceAction() {
        val length = uiState.value.currentStringValue.length
        if (length > 0) {
            _uiState.update {
                it.copy(currentStringValue = it.currentStringValue.substring(0, length - 1))
            }
        }
    }

    fun getPercentage() {
        val currentValue = uiState.value.currentStringValue
        try {
            if (currentValue.isNotEmpty()) {
                _uiState.update { it.copy(currentStringValue = (currentValue.toDouble() / 100).toString()) }
            }
        } catch (e: Exception) { _uiState.update { it.copy(currentStringValue = "Error") } }
    }

    fun equalsAction() {
        try{
            _uiState.update { it.copy(currentStringValue = calculateResults()) }
        } catch (e:Exception) {
            _uiState.update { it.copy(currentStringValue = "Error") }
        }
    }

    private fun calculateResults(): String {
        val expression = ExpressionBuilder(uiState.value.currentStringValue).build()
        val currentOperation = expression.evaluate()
        val result =
            if (uiState.value.currentStringValue.contains(".")) currentOperation
            else currentOperation.toInt()

        return result.toString()
    }
}