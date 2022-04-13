package com.nzf.turbozakat.equationparams

import com.nzf.turbozakat.Currency

class BusinessInvestmentParams(
        var doYouOwn: Boolean = true,
        var cashOnHand: Double = 0.0,
        var currencyOfCash: Currency = Currency.CAD,
        var amountReceivable: Double = 0.0,
        var currencyOfReceivable: Currency = Currency.CAD,
        var inventoryValue: Double = 0.0,
        var accountsPayable: Double = 0.0,
        override var label: String = ""
): EquationParams