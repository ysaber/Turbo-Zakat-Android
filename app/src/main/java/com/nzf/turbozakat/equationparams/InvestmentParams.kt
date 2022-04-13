package com.nzf.turbozakat.equationparams

import com.nzf.turbozakat.Currency

data class InvestmentParams(
        var areSharesOwned: Boolean = true,
        var investmentType: InvestmentType = InvestmentType.SHORT_TERM,
        var totalAmount: Double = 0.0,
        var currency: Currency = Currency.CAD,
        override var label: String = ""
        ): EquationParams {

    companion object {
        enum class InvestmentType {
            SHORT_TERM,
            LONG_TERM,
            SELLING,
            ILLIQUID
        }
    }

}