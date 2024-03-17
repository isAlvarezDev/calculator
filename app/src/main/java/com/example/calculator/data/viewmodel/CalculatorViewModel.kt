package com.example.calculator.data.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel: ViewModel() {
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
}