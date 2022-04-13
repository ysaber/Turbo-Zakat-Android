package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.equationparams.BusinessInvestmentParams
import java.io.Serializable

class BusinessInvestmentAsset(isBusInvOpinion1: Boolean): Serializable {

    private val equationParams = BusinessInvestmentParams()

        private val accountsPayableQuestion = InputQuestion(
                question = "If you have any accounts payable, enter that amount. Otherwise, enter 0.",
                progress = 97,
                isCashInput = true,
                inputanswer = InputAnswer(
                        nextQuestion = null,
                        calculation = { any ->
                            equationParams.accountsPayable = any as Double
                        }),
                isFinalQuestion = true,
                calculation = {
                    ((equationParams.cashOnHand / equationParams.currencyOfCash.conversionToCad)
                            + (equationParams.amountReceivable / equationParams.currencyOfReceivable.conversionToCad)
                            + (equationParams.inventoryValue)
                            - (equationParams.accountsPayable))
                })


        private val valueOfInventory = InputQuestion(
                question = if (isBusInvOpinion1) "What is the total wholesale value of your inventory?" else "What is the total retail value of your inventory?",
                progress = 90,
                isCashInput = true,
                inputanswer = InputAnswer(
                        nextQuestion = accountsPayableQuestion,
                        calculation = { any ->
                            equationParams.inventoryValue = any as Double
                        }))


        private val doYouOwnHaveInventoryAnswers = arrayListOf<Answer>(
                McrAnswer("Yes", valueOfInventory, { _ -> }),
                McrAnswer("No", accountsPayableQuestion, { _ -> equationParams.inventoryValue = 0.0 }))


        private val initialBusinessQuestionQuestion = McrQuestion(
                question = "Do you have any inventory?",
                progress = 80,
                answers = doYouOwnHaveInventoryAnswers)


        private val receivableCurrencyAnswers = getReceivableCurrencyAnswers()


        private val receivableCurrencyQuestion = McrQuestion(
                question = "What is the currency of the amount receivable you have?",
                progress = 70,
                answers = receivableCurrencyAnswers)


        private val accountsReceivableQuestion = InputQuestion(
                question = "What amount receivable do you have in this business?",
                progress = 60,
                isCashInput = true,
                inputanswer = InputAnswer(
                        nextQuestion = receivableCurrencyQuestion,
                        calculation = { any ->
                            equationParams.amountReceivable = any as Double
                        }))


        private val cashCurrencyAnswers = getCashCurrencyAnswers()


        private val cashCurrencyQuestion = McrQuestion(
                question = "What is the currency of the cash you are holding?",
                progress = 50,
                answers = cashCurrencyAnswers)


        private val inputCashQuestion = InputQuestion(
                question = "How much cash-on-hand are you holding in this business?",
                progress = 40,
                isCashInput = true,
                inputanswer = InputAnswer(cashCurrencyQuestion, { any ->
                    equationParams.cashOnHand = any as Double
                }))

        private val dontOwn = McrQuestion(
                question = "You don't owe any zakat on businesses you don't own.",
                progress = 90,
                answers = arrayListOf(),
                isFinalQuestion = true)

        private val doYouOwnAnswers = arrayListOf<Answer>(
                McrAnswer(
                        answer = "Yes",
                        nextQuestion = inputCashQuestion,
                        calculation = { _ -> equationParams.doYouOwn = true }),
                McrAnswer(
                        answer = "No",
                        nextQuestion = dontOwn,
                        calculation = { _ -> equationParams.doYouOwn = false }))

        private val initialBusinessQuestion = McrQuestion(
                question = "Do you own this business?",
                progress = 20,
                answers = doYouOwnAnswers)

    var labelQuestion = InputQuestion(
            question = "Give this business investment a name",
            progress = 10,
            isLabel = true,
            inputanswer = InputAnswer(
                    nextQuestion = initialBusinessQuestion,
                    calculation = { any -> equationParams.label = any as String}))


    val businessInvestment = Asset(
                enum = AssetEnum.BUSINESS,
                title = "Business Investment",
                initialQuestion = labelQuestion,
                equationParams = equationParams)


    private fun getCashCurrencyAnswers(): ArrayList<Answer> {
        val answers: ArrayList<Answer> = arrayListOf()
        Currency.CAD.getSortedCurrencies().forEach {
            val answer = McrAnswer(answer = it.name,
                    nextQuestion = accountsReceivableQuestion,
                    calculation = { _ ->
                            equationParams.currencyOfCash = it
                            accountsReceivableQuestion.currencyOfCash = it
                    })
            answers.add(answer)
        }
        return answers
    }

    private fun getReceivableCurrencyAnswers(): ArrayList<Answer> {
        val answers: ArrayList<Answer> = arrayListOf()
        Currency.CAD.getSortedCurrencies().forEach {
            val answer = McrAnswer(answer = it.name,
                    nextQuestion = initialBusinessQuestionQuestion,
                    calculation = { _ ->
                            equationParams.currencyOfReceivable = it
                    })
            answers.add(answer)
        }
        return answers
    }
}