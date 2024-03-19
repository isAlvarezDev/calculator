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
        val digitsOperators = digitsOperators()
        if (digitsOperators().isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result =  addSubtractCalculate(timesDivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (index in passedList.indices) {
            if (passedList[index] is Char && index != passedList.lastIndex) {
                val operator = passedList[index]
                val nextDigit = passedList[index + 1] as Float

                if (operator == '+') result += nextDigit
                if (operator == '−') result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('×') || list.contains('/')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (index in passedList.indices) {
            if (passedList[index] is Char && index != passedList.lastIndex && index < restartIndex) {
                val operator = passedList[index]
                val prevDigit = passedList[index - 1] as Float
                val nextDigit = passedList[index + 1] as Float

                when (operator) {
                    '×' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = index + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = index + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (index > restartIndex) newList.add(passedList[index])
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in uiState.value.currentStringValue) {
            if (character.isDigit() || character == '.') currentDigit += character
             else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "") list.add(currentDigit.toFloat())

        return list
    }
}
