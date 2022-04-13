package com.nzf.turbozakat.equationparams

class GoldParams(
        var isGold: Boolean = true,
        var value: Double = 0.0,
        var weight: Double = 0.0,
        var purity: Double = 0.0,
        override var label: String = ""
): EquationParams