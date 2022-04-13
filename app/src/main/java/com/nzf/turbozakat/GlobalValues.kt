package com.nzf.turbozakat

import android.content.Context

object GlobalValues {
    val SILVER_GRAMS_FOR_NISAB = 595
    val GOLD_GRAMS_FOR_NISAB = 85
    var goldPrice: Double = 0.0
    var silverPrice: Double = 1.0
    var nisabValue: Double = 0.0
    val onlyInQuickCheck = "Since you already accounted for this your cash calculation, we do not need to calculate it here."

    val accountedForQuestion = McrQuestion(
            question = GlobalValues.onlyInQuickCheck,
            progress = 80,
            answers = arrayListOf(),
            isFinalQuestion = true)

    fun setNisabBySilver() {
        nisabValue = silverPrice * SILVER_GRAMS_FOR_NISAB
    }

    fun setNisabByGold() {
        nisabValue = goldPrice * GOLD_GRAMS_FOR_NISAB
    }

    fun updateNisabValue(context: Context) {
        val nisabOptionSilver = context.getSharedPreferences(context.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getBoolean(KEY_NISAB_OPINION_1, true)
        if (nisabOptionSilver) {
            setNisabBySilver()
        } else {
            setNisabByGold()
        }
    }
}