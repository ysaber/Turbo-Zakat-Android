//package com.nzf.turbozakat
//
//import java.util.*
//
//object Currencies {
//
//    val currencies: ArrayList<Currency> = arrayListOf()
//
//    fun getCurrency(acronym: String): Currency? {
//        currencies.forEach {
//            if (it.acronym == acronym) {
//                return it
//            }
//        }
//        return null
//    }
//
//
//    fun getSortedCurrencies(): List<Currency> {
//        val currenciesHash = HashSet(currencies)
//        return currenciesHash.toList().sortedWith(kotlin.Comparator { o1, o2 -> o1.index - o2.index })
//    }
//
//}