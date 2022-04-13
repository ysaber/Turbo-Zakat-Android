package com.nzf.turbozakat.equationparams

class LiabilityParams (
        var expensesValue: Double = 0.0,
        var loanTakenValue: Double = 0.0,
        var loanGivenValue: Double = 0.0,
        override var label: String = "Liabilities"
): EquationParams