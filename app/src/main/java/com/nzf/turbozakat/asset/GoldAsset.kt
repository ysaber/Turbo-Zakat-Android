package com.nzf.turbozakat.asset

import com.nzf.turbozakat.*
import com.nzf.turbozakat.equationparams.GoldParams
import java.io.Serializable

class GoldAsset(isGoldOpinion1: Boolean) : Serializable {

    var PURITY_THRESHOLD = 0.5

    private val eq: GoldParams = GoldParams()

    private val enterSilverValueQuestion = InputQuestion(
            question = "Enter the value of your silver",
            progress = 90,
            isCashInput = true,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { eq.value = it as Double }),
            isFinalQuestion = true,
            calculation = {
                eq.value
            })

    private val enterSilverWeightQuestion = InputQuestion(
            question = "Enter the weight of your silver",
            progress = 90,
            isWeightInput = true,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { eq.weight = it as Double }),
            isFinalQuestion = true,
            calculation = {
                eq.weight * GlobalValues.silverPrice
            })

    private val silverOptionsAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Value",
                    nextQuestion = enterSilverValueQuestion,
                    calculation = { }),
            McrAnswer(
                    answer = "Weight",
                    nextQuestion = enterSilverWeightQuestion,
                    calculation = { }))


    private val enterGoldPurityQuestion = InputQuestion(
            question = "Enter the purity of your gold in carats",
            progress = 90,
            qualifier = { (it in (1..24)) && ((it % 1) == 0.0) },
            qualifierErrorMessage = "Please enter an integer between 1 and 24",
            isQualifiedQuestion = true,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { eq.purity = it as Double }),
            isFinalQuestion = true,
            calculation = {
                val purity = eq.purity
                if (purity <= PURITY_THRESHOLD) {
                    0.0
                } else {
                    (purity / 24) * eq.weight * GlobalValues.goldPrice
                }
            })

    private val enterGoldWeightAndPurityQuestion = InputQuestion(
            question = "Enter the weight of your gold",
            progress = 88,
            qualifier = { true }, //this is true b/c we just need to ensure it's a double
            qualifierErrorMessage = "Please enter a valid weight",
            isQualifiedQuestion = true,
            isWeightInput = true,
            inputanswer = InputAnswer(
                    nextQuestion = enterGoldPurityQuestion,
                    calculation = { eq.weight = it as Double }))

    private val enterGoldWeightQuestion = InputQuestion(
            question = "Enter the value of your gold",
            progress = 88,
            isCashInput = true,
            inputanswer = InputAnswer(
                    nextQuestion = null,
                    calculation = { eq.value = it as Double }),
            isFinalQuestion = true,
            calculation = {
                eq.value
            })


    private val goldOptionsAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Value",
                    nextQuestion = enterGoldWeightQuestion,
                    calculation = { }),
            McrAnswer(
                    answer = "Weight & Purity",
                    nextQuestion = enterGoldWeightAndPurityQuestion,
                    calculation = { }))

    private val goldOptionsQuestion = McrQuestion(
            question = "How would you like to calculate this asset?",
            progress = 66,
            blurb = "There is an accepted difference of opinion as to whether Zakat is payable on the weight of jewellery or value of the jewellery. If you follow the opinion about getting your Gold valued by a jeweller please enter the Dollar value provided by the jeweller in the calculator.\n" +
                    "Please keep in mind to get valuation done close to your Zakat payment date as prices change regularly. Moreover, only value pure gold/silver as other precious metals or jewels are not Zakatable assets. (such as diamonds or rubies etc)\n" +
                    "\nNote: It is easier to do it calculation by weight if you know the grams and karats of your precious metals.",
            answers = goldOptionsAnswers)

    private val silverOptionsQuestion = McrQuestion(
            question = "How would you like to calculate this asset?",
            progress = 66,
            blurb = "There is an accepted difference of opinion as to whether Zakat is payable on the weight of jewellery or value of the jewellery. If you follow the opinion about getting your Gold valued by a jeweller please enter the Dollar value provided by the jeweller in the calculator.\n" +
                    "Please keep in mind to get valuation done close to your Zakat payment date as prices change regularly. Moreover, only value pure gold/silver as other precious metals or jewels are not Zakatable assets. (such as diamonds or rubies etc)\n" +
                    "\nNote: It is easier to do it calculation by weight if you know the grams and karats of your precious metals.",
            answers = silverOptionsAnswers)

    private val goldOrSilverAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Gold",
                    nextQuestion = goldOptionsQuestion,
                    calculation = { _ -> eq.isGold = true }),
            McrAnswer(
                    answer = "Silver",
                    nextQuestion = silverOptionsQuestion,
                    calculation = { _ -> eq.isGold = false }))

    private val goldOrSilverQuestion = McrQuestion(
            question = "Select one",
            progress = 33,
            blurb = "There is an accepted difference of opinion as to whether Zakat is payable on jewellery. National Zakat Foundation adopts the view that Zakat should be paid on jewellery whether its worn or not.\n\nYou can set the opinion you follow under \"Rates & Zakat Fiqh Opinions\" on the homepage",
            answers = goldOrSilverAnswers)


    private val noUsedJewelery = McrQuestion(
            question = "You don't owe any zakat on jewelery that you use.",
            progress = 80,
            answers = arrayListOf(),
            isFinalQuestion = true)

    private val wasJeweleryUsedAnswers = arrayListOf<Answer>(
            McrAnswer(
                    answer = "Yes",
                    nextQuestion = noUsedJewelery,
                    calculation = { _ -> }),
            McrAnswer(
                    answer = "No",
                    nextQuestion = goldOrSilverQuestion,
                    calculation = { _ -> }))


    private val wasJeweleryUsedQuestion = McrQuestion(
            question = "Was the jewelery used?",
            progress = 20,
            answers = wasJeweleryUsedAnswers
    )


    var labelQuestion = InputQuestion(
            question = "Give gold/silver a name",
            progress = 10,
            isLabel = true,
            inputanswer = InputAnswer(
                    nextQuestion = if (isGoldOpinion1) goldOrSilverQuestion else wasJeweleryUsedQuestion,
                    calculation = { any -> eq.label = any as String }))


    val gold = Asset(
            enum = AssetEnum.GOLD,
            title = "Gold/Silver",
            initialQuestion = labelQuestion,
            equationParams = eq)


}