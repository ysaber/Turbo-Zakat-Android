package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.equationparams.SavingsPlanParams
import java.io.Serializable

class SavingsPlanAsset : Serializable {

    private val eq: SavingsPlanParams = SavingsPlanParams()


    private val enterRrspTaxQuestion = InputQuestion(
            question = "What's the total tax on your RRSP if you were to withdraw today",
            progress = 95,
            isCashInput = true,
            isFinalQuestion = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(nextQuestion = null, calculation = { eq.taxes = it as Double }),
            calculation = {
                eq.rrspAmount - eq.taxes
            })

    private val enterRespTaxQuestion = InputQuestion(
            question = "What's the total tax on your RESP if you were to withdraw today",
            progress = 95,
            isCashInput = true,
            isFinalQuestion = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(nextQuestion = null, calculation = { eq.taxes = it as Double }),
            calculation = {
                eq.respAmount - eq.taxes
            })


    private val noZakatForInaccessibleFunds = McrQuestion(
            question = "You don't owe any zakat on funds that you don't have access to.",
            progress = 85,
            answers = arrayListOf(),
            isFinalQuestion = true)


    private val rrspAccessibleAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = enterRrspTaxQuestion),
            McrAnswer(
                    answer = "No",
                    nextQuestion = noZakatForInaccessibleFunds))

    private val respAccessibleAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = enterRespTaxQuestion),
            McrAnswer(
                    answer = "No",
                    nextQuestion = noZakatForInaccessibleFunds))

    private val rrspAccessibleQuestion = McrQuestion(
            question = "Can you access the funds instantly (even with penalties, taxes or other expenses)?",
            progress = 80,
            answers = rrspAccessibleAnswers,
            isFinalQuestion = false)


    private val respAccessibleQuestion = McrQuestion(
            question = "Can you access the funds instantly (even with penalties, taxes or other expenses)?",
            progress = 80,
            answers = respAccessibleAnswers,
            isFinalQuestion = false)


    private val enterRrspFundQuestion = InputQuestion(
            question = "Enter the total RRSP fund in CAD",
            progress = 66,
            isCashInput = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = rrspAccessibleQuestion,
                    calculation = { eq.rrspAmount = it as Double }))


    private val enterRespFundQuestion = InputQuestion(
            question = "Enter the total RESP fund in CAD",
            progress = 66,
            isCashInput = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = respAccessibleQuestion,
                    calculation = { eq.respAmount = it as Double }))


    private val enterTfsaFundQuestion = InputQuestion(
            question = "Enter the total TFSA fund in CAD",
            progress = 66,
            isCashInput = true,
            isFinalQuestion = true,
            currencyOfCash = Currency.CAD,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { eq.tfsaAmount = it as Double }),
            calculation = {
                eq.tfsaAmount
            })



    private val tfsaConsiderAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "No",
                    nextQuestion = enterTfsaFundQuestion),
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = GlobalValues.accountedForQuestion))


    private val tfsaConsiderQuestion = McrQuestion(
            question = "Did you consider this TFSA amount in your cash calculation?",
            progress = 40,
            answers = tfsaConsiderAnswers)



    private val natureOfPropertyAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "RRSP",
                    nextQuestion = enterRrspFundQuestion),
            McrAnswer(
                    answer = "RESP",
                    nextQuestion = enterRespFundQuestion),
            McrAnswer(
                    answer = "TFSA",
                    nextQuestion = tfsaConsiderQuestion))

    private val whichPlanQuestion = McrQuestion(
            question = "Select a savings plan",
            progress = 20,
            answers = natureOfPropertyAnswers)

    var labelQuestion = InputQuestion(
            question = "Give this savings plan item a name",
            progress = 10,
            isLabel = true,
            inputanswer = InputAnswer(
                    nextQuestion = whichPlanQuestion,
                    calculation = { eq.label = it as String }))

    val savingsPlan = Asset(
            enum = AssetEnum.SAVINGSPLAN,
            title = "Canadian Savings Plan",
            initialQuestion = labelQuestion,
            equationParams = eq)
}