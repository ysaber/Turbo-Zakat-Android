//package com.nzf.turbozakat.ui
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.view.View
//import com.nzf.turbozakat.*
//import com.nzf.turbozakat.ui.AssetActivity.Companion.KEY_ASSET
//import com.nzf.turbozakat.ui.AssetActivity.Companion.KEY_QUESTION
//import com.nzf.turbozakat.asset.Asset
//import com.nzf.turbozakat.asset.AssetEnum
//import com.nzf.turbozakat.asset.ZAKAT_RATIO
//import kotlinx.android.synthetic.main.activity_calculate_zakat.*
//import org.jetbrains.anko.alert
//import org.jetbrains.anko.intentFor
//import org.jetbrains.anko.noButton
//import org.jetbrains.anko.yesButton
//
//class CalculateZakatActivity : AppCompatActivity() {
//
//    @SuppressLint("SetTextI18n")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_calculate_zakat)
//
//        val finalQuestion: Question = intent?.extras?.getSerializable(KEY_QUESTION) as Question
//        val asset: Asset = intent?.extras?.getSerializable(KEY_ASSET) as Asset
//
//        val finalAmountOwing = finalQuestion.finalCalculation.invoke()
//        asset.calculatedAmount = if (asset.isType(AssetEnum.LIABILITIES)) {
//            finalAmountOwing
//        } else {
//            if (finalAmountOwing > 0) {
//                finalAmountOwing
//            } else {
//                0.0
//            }
//        }
//
//        val assetTitle = asset.title.toLowerCase()
//
//        val zakatAmount = asset.calculatedAmount * ZAKAT_RATIO
//
//        questionTextView.text = if (zakatAmount < 0) {
//            "You have a credit of $${Math.abs(zakatAmount).format(2)} (CAD) from your $assetTitle"
//        } else {
//            "You owe $${zakatAmount.format(2)} (CAD) on your $assetTitle"
//        }
//
//        if (QuickCheckSession.isFullZakatCheck) {
//
//            QuickCheckSession.completedAssets.add(asset)
//
//            if (QuickCheckSession.assets.size > 1) {
//
//                textNotDone.text = "Add more ${asset.title}"
//                textDone.text = "Next: ${QuickCheckSession.getSecondLowestIndexedAsset().title}"
//
//                cardNotDone.setOnClickListener {
//                    asset.questions.clear()
//                    val intent2 = Intent(this, AssetActivity::class.java)
//                    startActivity(intent2)
//                    setResult(MainActivity.FINISH_ACTIVITY)
//                    finish()
//                }
//
//                cardDone.setOnClickListener {
//                    QuickCheckSession.removeLowestIndexedAsset()
//                    val intent = Intent(this, AssetActivity::class.java)
//                    startActivity(intent)
//                    setResult(MainActivity.FINISH_ACTIVITY)
//                    finish()
//                }
//
//
//            } else {
//                if (asset.isType(AssetEnum.LIABILITIES)) {
//                    cardNotDone.visibility = View.GONE
//                } else {
//                    textNotDone.text = "Add more ${asset.title}"
//                }
//                textDone.text = "Next: Summary"
//
//                cardNotDone.setOnClickListener {
//                    asset.questions.clear()
//                    val intent2 = Intent(this, AssetActivity::class.java)
//                    startActivity(intent2)
//                    setResult(MainActivity.FINISH_ACTIVITY)
//                    finish()
//                }
//
//                cardDone.setOnClickListener {
//                    val intent = Intent(this, SummaryActivity::class.java)
//                    startActivity(intent)
//                    setResult(MainActivity.FINISH_ACTIVITY)
//                    finish()
//                }
//            }
//
//        } else {
//            textNotDone.text = "Add more ${asset.title}"
//            QuickCheckSession.assets.add(asset)
//
//            cardNotDone.setOnClickListener {
//                asset.questions.clear()
//                val intent = intentFor<AssetActivity>(Pair(KEY_ASSET, asset))
//                startActivity(intent)
//                setResult(MainActivity.FINISH_ACTIVITY)
//                finish()
//            }
//
//            cardDone.setOnClickListener {
//                val intent = intentFor<SummaryActivity>()
//                startActivity(intent)
//                setResult(MainActivity.FINISH_ACTIVITY)
//                finish()
//            }
//        }
//
//    }
//
//
//    override fun onBackPressed() {
//        alert("Pressing back will lose all progress, and your information will have to be re-entered.", "Are you sure?") {
//            yesButton { super.onBackPressed() }
//            noButton { it.dismiss() }
//        }.show()
//    }
//}
//
//infix fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)
