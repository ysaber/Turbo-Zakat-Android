package com.nzf.turbozakat.equationparams

import com.nzf.turbozakat.Currency

class CashParams(var currency: Currency = Currency.CAD, var amount: Double = 0.0, override var label: String = ""): EquationParams