package com.nzf.turbozakat

class InputQuestion(question: String, progress: Int,
                    val inputanswer: InputAnswer,
                    val isCashInput: Boolean = false,
                    val isWeightInput: Boolean = false,
                    val isQualifiedQuestion: Boolean = false,
                    val qualifier: (Double) -> Boolean = { true },
                    val qualifierErrorMessage: String = "",
                    val isLabel: Boolean = false,
                    blurb: String = "",
                    var currencyOfCash: Currency = Currency.CAD,
                    isFinalQuestion: Boolean = false,
                    calculation: () -> Double = { 0.0 })
    : Question(question, progress, isFinalQuestion, blurb, calculation)