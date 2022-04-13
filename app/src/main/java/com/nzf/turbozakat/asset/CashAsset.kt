package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.Currency
import com.nzf.turbozakat.equationparams.CashParams
import java.io.Serializable
import java.util.*

class CashAsset : Serializable {


    private val eq: CashParams = CashParams()

    private var howMuchQuestion = InputQuestion(
            question = "How much do you have?",
            progress = 66,
            blurb = "Enter cash amount",
            isCashInput = true,
            currencyOfCash = eq.currency,
            inputanswer = InputAnswer(
                    calculation = { eq.amount = it as Double },
                    nextQuestion = null),
            isFinalQuestion = true,
            calculation = {
                eq.amount / eq.currency.conversionToCad
            })

    private var initialCashAnswer = getCashAnswers()


    private var initialCashQuestion = McrQuestion(
            question = "Which currency?",
            progress = 33,
            blurb = "Select one currency only, you will get a chance to add more currencies later (if applicable).",
            answers = initialCashAnswer)

    private var labelQuestion = InputQuestion(
            question = "Give this cash a name",
            progress = 10,
            blurb = "At the time of the Prophet Muhammad (SAW) the currency of the day was gold dinars or silver dirhams and Zakat was due on them. Since paper money has now become an accepted and commonly used means of transacting, Zakat is due on wealth held in this form.\n\n" +
                    "In calculating all your cash holdings, don't forget to include all your bank accounts.\n\n" +
                    "If you hold cash in various currencies, you will have the option to select them in the next few steps.",
            isLabel = true,
            inputanswer = InputAnswer(
                    nextQuestion = initialCashQuestion,
                    calculation = { eq.label = it as String }))

    var cash = Asset(
            enum = AssetEnum.CASH,
            title = "Cash",
            initialQuestion = labelQuestion,
            equationParams = eq)


    private fun getCashAnswers(): ArrayList<Answer> {
        val answers: ArrayList<Answer> = arrayListOf()
        Currency.CAD.getSortedCurrencies().forEach {
            val answer = McrAnswer(answer = it.name,
                    nextQuestion = howMuchQuestion,
                    calculation = { _ ->
                        run {
                            eq.currency = it
                            howMuchQuestion.currencyOfCash = it
                        }
                    })
            answers.add(answer)
        }
        return answers
    }

}