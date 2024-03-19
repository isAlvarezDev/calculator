package com.example.calculator.data.local

import com.example.calculator.data.model.Symbol

object LocalSymbolDataProvider {
    val clearSymbol =  Symbol("AC")
    val backspaceSymbol = Symbol("⌫")
    val percentageSymbol = Symbol("%")
    val operands = listOf(
        Symbol("/"),
        Symbol("×"),
        Symbol("−"),
        Symbol("+"),
    )
    val equalSymbol = Symbol("=")

}