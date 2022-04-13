package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.GlobalValues.accountedForQuestion
import com.nzf.turbozakat.equationparams.PropertyParams
import java.io.Serializable

class PropertyAsset(isPropertyOpinion1: Boolean) : Serializable {

    private val eq: PropertyParams = PropertyParams()

    private val miscExpensesQuestion = InputQuestion(
            question = "How much miscellaneous expenses are you paying monthly?",
            progress = 95,
            isCashInput = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { any ->
                        eq.monthlyMiscExpenses = any as Double
                    }),
            isFinalQuestion = true, calculation = {
        ((eq.monthlyRent
                - eq.annualPropertyTax
                - eq.monthlyMiscExpenses
                - eq.monthlyMaintenance) * 12)
    })

    private val propertyTaxQuestion = InputQuestion(
            question = "How much property tax are you paying monthly?",
            progress = 85,
            isCashInput = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = miscExpensesQuestion,
                    calculation = { any ->
                        eq.annualPropertyTax = any as Double
                    }))

    private val maintenanceQuestion = InputQuestion(
            question = "How much maintenance are you paying monthly?",
            progress = 75,
            isCashInput = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = propertyTaxQuestion,
                    calculation = { eq.monthlyMaintenance = it as Double }))

    private val rentingQuestion = InputQuestion(
            question = "How much are you renting this property for (monthly)?",
            progress = 66,
            isCashInput = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = maintenanceQuestion,
                    calculation = { eq.monthlyRent = it as Double }))

    private val markedForSaleQuestionOnRetailValue = InputQuestion(
            question = "How much are you selling this property for?",
            progress = 80,
            isCashInput = true,
            currencyOfCash = Currency.CAD, //TODO WE SHOULD BE ASKING THIS
            inputanswer = InputAnswer(null, { eq.salePrice = it as Double }),
            isFinalQuestion = true,
            calculation = {
                eq.salePrice
            })



    private val notSelling = McrQuestion(
            question = "You don't owe any zakat on property you have no intention to sell.",
            progress = 80,
            answers = arrayListOf(),
            isFinalQuestion = true)


    private val primaryResidence = McrQuestion(
            question = "You don't owe any zakat on property you are residing in.",
            progress = 80,
            answers = arrayListOf(),
            isFinalQuestion = true)

    private val consideredRentalInCashAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = accountedForQuestion),
            McrAnswer(
                    answer = "No",
                    nextQuestion = rentingQuestion))

    private val consideredRentalInCashQuestion = McrQuestion(
            question = "Did you include your income/expenses from this rental in your cash calculation already?",
            progress = 80,
            answers = consideredRentalInCashAnswers,
            isFinalQuestion = false)


    private val consideredSaleInCashAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = accountedForQuestion),
            McrAnswer(
                    answer = "No",
                    nextQuestion = rentingQuestion))

    private val consideredSaleInCashQuestion = McrQuestion(
            question = "Did you include your income from this sale in your cash calculation already?",
            progress = 80,
            answers = consideredSaleInCashAnswers)


    private val natureOfPropertyAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Renting to someone",
                    nextQuestion =
                        consideredRentalInCashQuestion
            ),
            McrAnswer(
                    answer = "Marked for sale",
                    nextQuestion = if (isPropertyOpinion1) {
                        consideredSaleInCashQuestion
                    } else {
                        markedForSaleQuestionOnRetailValue
                    }),
            McrAnswer(
                    answer = "No intention of selling / Unsure",
                    nextQuestion = notSelling),
            McrAnswer(
                    answer = "Primary residence",
                    nextQuestion = primaryResidence))

    private val natureOfPropertyQuestion = McrQuestion(
            question = "What is the nature of this property?",
            progress = 40,
            answers = natureOfPropertyAnswers)

    private val dontOwn = McrQuestion(
            question = "You don't owe any zakat on property you don't own.",
            progress = 80,
            answers = arrayListOf(),
            isFinalQuestion = true)


    private val doYouOwnAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = natureOfPropertyQuestion,
                    calculation = { eq.isItOwned = true }),
            McrAnswer(
                    answer = "No",
                    nextQuestion = dontOwn,
                    calculation = { eq.isItOwned = false }))


    private val propertyQuestion = McrQuestion(
            question = "Do you own this property?",
            progress = 20,
            answers = doYouOwnAnswers)


    var labelQuestion = InputQuestion(
            question = "Give this property a name",
            progress = 10,
            blurb = "Property is a very interesting section when it comes to Zakat. You may have to pay thousands to nothing depending on what you are doing with that property in Zakat. Proceed and let's find out which bracket you fall under.",
            isLabel = true,
            inputanswer = InputAnswer(
                    nextQuestion = propertyQuestion,
                    calculation = { eq.label = it as String }))


    val property = Asset(
            enum = AssetEnum.PROPERTY,
            title = "Property",
            initialQuestion = labelQuestion,
            equationParams = eq)
}
