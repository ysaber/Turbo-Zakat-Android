package com.nzf.turbozakat.equationparams

data class SavingsPlanParams(
        var tfsaAmount: Double = 0.0,
        var respAmount: Double = 0.0,
        var rrspAmount: Double = 0.0,
        var taxes: Double = 0.0,
        override var label: String = ""
) : EquationParams