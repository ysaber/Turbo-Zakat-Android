package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.equationparams.LiabilityParams
import java.io.Serializable

class Liabilities : Serializable {

    private val eq: LiabilityParams = LiabilityParams()

    private val nextAnswer = arrayListOf<Answer>()

    private val nextQuestion = McrQuestion(
            question = "Click next to continue",
            progress = 90,
            isFinalQuestion = true,
            answers = nextAnswer,
            calculation = {
                (eq.loanGivenValue - eq.expensesValue - eq.loanTakenValue)
            })


    private var loanValueQuestion = InputQuestion(
            question = "What is the total value of the loan that you are expecting to receive?",
            progress = 80,
            isCashInput = true,
            isFinalQuestion = true,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { eq.loanGivenValue = it as Double }),
            calculation = {
                (eq.loanGivenValue - eq.expensesValue - eq.loanTakenValue)
            })


    private val anyLoansAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "No",
                    nextQuestion = nextQuestion),
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = loanValueQuestion))


    private val anyLoansQuestion = McrQuestion(
            question = "Have you given any loans that you are expecting to have repaid by the end of the lunar year?\n(if you are only planning on getting back a portion of the loan, click yes and enter that amount)",
            progress = 60,
            answers = anyLoansAnswers)


    var debtRepayQuestion = InputQuestion(
            question = "What is the total amount that you will repay?",
            progress = 50,
            isCashInput = true,
            inputanswer = InputAnswer(
                    nextQuestion = anyLoansQuestion,
                    calculation = { eq.loanTakenValue = it as Double }))


    private val anyDebtsAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "No",
                    nextQuestion = anyLoansQuestion),
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = debtRepayQuestion))


    private val anyDebtsQuestion = McrQuestion(
            question = "Have you taken out any loans (do not include mortgage) that you will pay off by the end of the lunar year?\n(if you are only planning on paying back a portion of a loan, click yes and enter that amount)",
            progress = 40,
            answers = anyDebtsAnswers)


    var expensesValueQuestion = InputQuestion(
            question = "What is the total value of the expenses?",
            progress = 30,
            isCashInput = true,
            inputanswer = InputAnswer(
                    nextQuestion = anyDebtsQuestion,
                    calculation = { eq.expensesValue = it as Double }))

    private val anyExpensesAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "No",
                    nextQuestion = anyDebtsQuestion),
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = expensesValueQuestion))


    private val anyExpensesQuestion = McrQuestion(
            question = "Do you have any expenses that you will be paying off by the end of the lunar year (for example: mortgage payments, car financing, etc)?",
            progress = 10,
            answers = anyExpensesAnswers)



    val liabilities = Asset(
            enum = AssetEnum.LIABILITIES,
            title = "Liabilities",
            initialQuestion = anyExpensesQuestion,
            equationParams = eq)
}