package com.nzf.turbozakat

import java.io.Serializable

enum class Currency(private val index: Int, val acronym: String, val symbol: String, var conversionToCad: Double) : Serializable {

    CAD(0, "CAD", "$", 1.0),
    USD(1, "USD", "$", 0.0),
    EUR(2, "EUR", "€", 0.0),
    GBP(3, "GBP", "£", 0.0);


    fun getSortedCurrencies(): List<Currency> {
        val currenciesHash = HashSet(Currency.values().toMutableList())
        return currenciesHash.toList().sortedWith(kotlin.Comparator { o1, o2 -> o1.index - o2.index })
    }

}