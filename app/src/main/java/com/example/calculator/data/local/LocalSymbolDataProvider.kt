package com.example.calculator.data.local

import com.example.calculator.data.model.Symbol

object LocalSymbolDataProvider {
    val clearSymbol =  Symbol("AC")
    val percentageSymbol = Symbol("%")
    val divideSymbol = Symbol("/")
    val multiplySubtractAndAddSymbols = listOf(
        Symbol("x"),
        Symbol("-"),
        Symbol("+"),
    )
    val equalSymbol = Symbol("=")

}