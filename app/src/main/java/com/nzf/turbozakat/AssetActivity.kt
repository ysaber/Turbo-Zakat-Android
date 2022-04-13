package com.nzf.turbozakat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.nzf.turbozakat.asset.Asset
import kotlinx.android.synthetic.main.activity_asset.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.regex.Pattern


class AssetActivity : AppCompatActivity() {

    companion object {
        const val KEY_ASSET = "asset_key"
        const val KEY_QUESTION = "question_key"
        const val KEY_NEXT_QUESTION = "next_question_key"
        const val REQUEST_CODE_NEXT_QUESTION = 0
        const val REQUEST_CODE_CALCULATE_ZAKAT = 1
    }

    var currency: Currency = Currency.CAD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset)

        val extras = intent?.extras
        val asset: Asset
        asset = if (extras != null && extras.containsKey(KEY_ASSET)) {
            extras.getSerializable(KEY_ASSET) as Asset
        } else if (QuickCheckSession.isFullZakatCheck) {
            QuickCheckSession.getLowestIndexedAsset()
        } else {
            throw IllegalArgumentException("Couldn't find asset!")
        }


        titleTextView.text = asset.title
        questionTextView.text = asset.initialQuestion?.question
        progress_bar.progress = asset.initialQuestion?.progress ?: 0

        val answers: List<McrAnswer>?

        val question: Question? = intent?.extras?.getSerializable(KEY_NEXT_QUESTION) as? Question
                ?: asset.initialQuestion


        if (question != null) {
            questionTextView.text = question.question
            progress_bar.progress = question.progress

            if (question is InputQuestion && question.isLabel) {
                assetsEditText.inputType = InputType.TYPE_CLASS_TEXT
            }
        }


        //TODO Fix the below with a where-block

        //If is multiple choice
        if (question != null && question is McrQuestion && question.answers != null) {
            val adapter = McrQuestionAdapter(asset, question, question.answers)
            assetsListView.adapter = adapter
            assetsListView.visibility = View.VISIBLE
            assetsEditText.visibility = View.GONE
        } else {

            if (question != null && question is InputQuestion && question.isCashInput) {
                assetsEditText.requestFocus()
            } else {
                assetsEditText.requestFocus()
            }

            answers = if (question != null) {
                if (question is McrQuestion) {
                    question.answers!! as List<McrAnswer>
                } else {
                    null
                }
            } else {
                if (asset.initialQuestion is McrQuestion) {
                    (asset.initialQuestion as McrQuestion).answers!! as List<McrAnswer>
                } else {
                    null
                }
            }

            if (answers != null) {
                val adapter = McrQuestionAdapter(asset, question, answers)
                assetsListView.adapter = adapter
                assetsListView.visibility = View.VISIBLE
                assetsEditText.visibility = View.GONE
            }
        }



        if (asset.questions.isNotEmpty()) {
            btnPrev.setOnClickListener {

                val prevQuestion = asset.questions.last()
                asset.questions.remove(asset.questions.last())

                val intent = intentFor<AssetActivity>(Pair(KEY_ASSET, asset), Pair(KEY_NEXT_QUESTION, prevQuestion))
                startActivity(intent)
                finish()
            }
        } else {
            btnPrev.visibility = View.GONE
        }


        //Calculate when click next if final
        if (question?.isFinalQuestion == true) {

            assetsEditText.setOnEditorActionListener { _, _, _ ->
                nextForFinalQuestion(asset, question)
                true
            }

            btnNext.setOnClickListener {
                nextForFinalQuestion(asset, question)
            }

            //InputQuestions only
        } else {

            assetsEditText.setOnEditorActionListener { _, _, _ ->
                nextForInputQuestion(asset, question as InputQuestion)
                true
            }

            if (question is InputQuestion) {
                btnNext.setOnClickListener {
                    nextForInputQuestion(asset, question as InputQuestion)
                }
            }
        }


        if (question is InputQuestion) {
            if (question.isCashInput) {
                assetsEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                assetsEditText.filters = arrayOf<InputFilter>(CurrencyFormatInputFilter())
                prefix.visibility = View.VISIBLE
                prefix.text = question.currencyOfCash.symbol
//                prefix.text = question.currencyOfCash.getSumbol()
            } else if (question.isWeightInput) {
                suffix.visibility = View.VISIBLE
            }

            assetsEditText.postDelayed({
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.showSoftInput(assetsEditText, 0)
            }, 50)

        }


        blurb.text = question?.blurb
    }


    private fun nextForInputQuestion(asset: Asset, question: InputQuestion) {
        if (TextUtils.isEmpty(assetsEditText.text)) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()

        } else {

            var isDouble = true
            try {
                assetsEditText.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                isDouble = false
            }

            if (question.isQualifiedQuestion && (!isDouble || !question.qualifier(assetsEditText.text.toString().toDouble()))) {
                Toast.makeText(this, question.qualifierErrorMessage, Toast.LENGTH_SHORT).show()
            } else {

                asset.questions.add(question)

                var doubleVal = 0.0
                try {
                    doubleVal = assetsEditText.text.toString().toDouble()
                } catch (ignore: Exception) {
                    //If we're here, we're not working with a Double, so ignore
                }

                if (question.isLabel) {
                    question.inputanswer.calculation.invoke(assetsEditText.text.toString())
                } else if (doubleVal > 0.0) {
                    question.inputanswer.calculation.invoke(doubleVal)
                }

                val intent = intentFor<AssetActivity>(Pair(KEY_ASSET, asset), Pair(KEY_NEXT_QUESTION, question.inputanswer.nextQuestion))
                startActivityForResult(intent, REQUEST_CODE_NEXT_QUESTION)
            }
        }
    }


    private fun isDigitsAndDotOnly(string: String): Boolean {
        val noDots = string.replace('.', '0')
        return TextUtils.isDigitsOnly(noDots)
    }


    private fun nextForFinalQuestion(asset: Asset, question: Question) {
        if (question is InputQuestion && (TextUtils.isEmpty(assetsEditText.text) || !isDigitsAndDotOnly(assetsEditText.text.toString()))) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()

        } else {

            if (question is InputQuestion && question.isQualifiedQuestion
                    &&!question.qualifier(assetsEditText.text.toString().toDouble())) {
                Toast.makeText(this, question.qualifierErrorMessage, Toast.LENGTH_SHORT).show()
            } else {

                var doubleVal = 0.0
                try {
                    doubleVal = assetsEditText.text.toString().toDouble()
                } catch (ignore: Exception) {
                    //If we're here, we're not working with a Double, so ignore
                }

                if (question is InputQuestion && doubleVal > 0.0) {
                    question.inputanswer.calculation.invoke(doubleVal)
                }


                val intent = intentFor<CalculateZakatActivity>(Pair(KEY_ASSET, asset), Pair(KEY_QUESTION, question))
                startActivityForResult(intent, REQUEST_CODE_CALCULATE_ZAKAT)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == MainActivity.FINISH_ACTIVITY) {
            setResult(MainActivity.FINISH_ACTIVITY)
            finish()
        }
    }


    inner class CurrencyFormatInputFilter : InputFilter {

        private var mPattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?")

        override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dstart: Int,
                dend: Int): CharSequence? {

            val result = (dest.subSequence(0, dstart).toString()
                    + source.toString()
                    + dest.subSequence(dend, dest.length))

            val matcher = mPattern.matcher(result)

            return if (!matcher.matches()) dest.subSequence(dstart, dend) else null

        }
    }

    override fun onBackPressed() {
        alert("Pressing back will lose all progress, and your information will have to be re-entered.", "Are you sure?") {
            yesButton { super.onBackPressed() }
            noButton { it.dismiss() }
        }.show()
    }
}
