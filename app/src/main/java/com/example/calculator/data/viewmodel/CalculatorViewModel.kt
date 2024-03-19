package com.example.calculator.data.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CalculatorUiState()
    )
    val uiState: StateFlow<CalculatorUiState> = _uiState

    fun updateCurrentValue(digit: String) {
        _uiState.update {
            it.copy(currentStringValue = it.currentStringValue + digit)
        }
    }

    fun removeCurrentValue() {
        _uiState.update {
            it.copy(currentStringValue = "")
        }
    }

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
        } catch (e: NumberFormatException) {
            _uiState.update { it.copy(currentStringValue = "Error") }
        } catch (e: ArithmeticException) {
            _uiState.update { it.copy(currentStringValue = "Error") }
        }
    }
}
