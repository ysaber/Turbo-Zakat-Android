package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.equationparams.InvestmentParams
import java.io.Serializable

class InvestmentAsset : Serializable {

        private val eq: InvestmentParams = InvestmentParams()


        private val shortTermQuestion = InputQuestion(
                question = "How much are you shares/investment worth?",
                progress = 90,
                isCashInput = true,
                inputanswer = InputAnswer(
                        nextQuestion = null,
                        calculation = { any ->
                            eq.totalAmount = any as Double
                        }),
                isFinalQuestion = true,
                calculation = {
                    eq.totalAmount / eq.currency.conversionToCad
                })

        private val shortTermCurrencyAnswers = getShortTermCurrencyAnswers()


        private val shortTermCurrencyQuestion = McrQuestion(
                question = "What is the currency of these shares?",
                progress = 60,
                answers = shortTermCurrencyAnswers)


        private val sellingQuestion = InputQuestion(
                question = "How much are you shares/investment worth after taxes and fees?",
                progress = 90,
                isCashInput = true,
                inputanswer = InputAnswer(
                        nextQuestion = null,
                        calculation = { any ->
                            eq.totalAmount = any as Double
                        }),
                isFinalQuestion = true,
                calculation = {
                    eq.totalAmount
                })

        private val cantsell = McrQuestion(
                question = "You don't owe any zakat on shares/investments that you can't sell.",
                progress = 90,
                answers = arrayListOf(),
                isFinalQuestion = true,
                calculation = { 0.0 })



    private val consideredSaleInCashAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = GlobalValues.accountedForQuestion),
            McrAnswer(
                    answer = "No",
                    nextQuestion = sellingQuestion))

    private val consideredSaleInCashQuestion = McrQuestion(
            question = "Did you include your income from this sale in your cash calculation already?",
            progress = 80,
            answers = consideredSaleInCashAnswers,
            isFinalQuestion = false)


    private val natureAnswers = arrayListOf<Answer>(
                McrAnswer(
                        answer = "Holding",
                        nextQuestion = shortTermCurrencyQuestion,
                        calculation = { eq.investmentType = InvestmentParams.Companion.InvestmentType.SHORT_TERM }),
                McrAnswer(
                        answer = "Can't Be Sold (Illiquid)",
                        nextQuestion = cantsell,
                        calculation = { eq.investmentType = InvestmentParams.Companion.InvestmentType.ILLIQUID }),
                McrAnswer(
                        answer = "Selling",
                        nextQuestion = consideredSaleInCashQuestion,
                        calculation = { eq.investmentType = InvestmentParams.Companion.InvestmentType.SELLING }))


        private val natureOfShares = McrQuestion(
                question = "What is the nature of these shares/investments?",
                progress = 40,
                answers = natureAnswers)

        private val dontown = McrQuestion(
                question = "You don't owe any zakat on shares/investments that you don't own.",
                progress = 80,
                answers = arrayListOf(),
                isFinalQuestion = true)


        private val initialAnswers = arrayListOf<Answer>(
                McrAnswer(
                        answer = "Yes",
                        nextQuestion = natureOfShares,
                        calculation = { _ -> eq.areSharesOwned = true }),
                McrAnswer(
                        answer = "No",
                        nextQuestion = dontown,
                        calculation = { _ -> eq.areSharesOwned = false }))


        private val initialQuestion = McrQuestion(
                question = "Do you own these shares/investments?",
                progress = 20,
                answers = initialAnswers)


    var labelQuestion = InputQuestion(
            question = "Give these shares/investments a name",
            progress = 10,
            isLabel = true,
            blurb = "There are four possibilities depending on your intention with these investments and also the types/frequency of transactions you are involved in with each asset you own.",
            inputanswer = InputAnswer(
                    nextQuestion = initialQuestion,
                    calculation = { any -> eq.label = any as String}))


    val investment = Asset(
                enum = AssetEnum.INVESTMENT,
                title = "Shares/Investment",
                initialQuestion = labelQuestion,
                equationParams = eq)


    private fun getShortTermCurrencyAnswers(): ArrayList<Answer> {
        val answers: ArrayList<Answer> = arrayListOf()
        Currency.CAD.getSortedCurrencies().forEach {
            val answer = McrAnswer(answer = it.name,
                    nextQuestion = shortTermQuestion,
                    calculation = { _ ->
                        run {
                            eq.currency = it
                            shortTermQuestion.currencyOfCash = it
                        }
                    })
            answers.add(answer)
        }
        return answers
    }

}