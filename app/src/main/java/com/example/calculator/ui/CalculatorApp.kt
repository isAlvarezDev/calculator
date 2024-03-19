package com.example.calculator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.data.viewmodel.CalculatorViewModel
import com.example.calculator.ui.screens.HomeScreen

@Composable
fun CalculatorApp() {
    val viewModel: CalculatorViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    HomeScreen(
        text = uiState.currentStringValue,
        onClickClear = { viewModel.removeCurrentValue() },
        onClickBackspace = { viewModel.backspaceAction() },
        onClickPercentage = { viewModel.getPercentage() },
        onClickDigit = { viewModel.updateCurrentValue(it) },
        onClickEqual = {  })
}