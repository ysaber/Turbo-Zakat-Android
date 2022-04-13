package com.nzf.turbozakat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_learn.*


class LearnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
    }

    fun onClick(v: View) {
        val url: String = when (v) {
            btnHowToCalcZakat -> "https://www.youtube.com/watch?v=XrhT7bY8t5c"
            btnWhatIsZakat -> "https://www.youtube.com/watch?v=JYfJI2f6w5w"
            btnWhatIsNisab -> "https://www.youtube.com/watch?v=4fe3uyP2oX4"
            btnZakatOnIndividualOrWealth -> "https://www.youtube.com/watch?v=UDlrrNzmBEc"
            btnHowToEstablishZakatYear -> "https://www.youtube.com/watch?v=gd85jiVQotM"
            btnHowToCalcZakatOnProperty -> "https://www.youtube.com/watch?v=yd67d9XCru8"
            btnHowToCalcZakatOnJewelery -> "https://www.youtube.com/watch?v=v1_Fxw0aFxA"
            btnIsZakatDueUponJewelry -> "https://www.youtube.com/watch?v=1rjYZlxYeY4"
            btnHowToCalcZakatOnRRSP -> "https://www.youtube.com/watch?v=99bjSL-pOlc"
            btnHowToCalcZakatOnStocks -> "https://www.youtube.com/watch?v=4HVJnKUk79M"
            btnHowToCalcZakatOnInvestments -> "https://www.youtube.com/watch?v=3826uSq9FPQ"
            else -> ""
        }

        if (!TextUtils.isEmpty(url)) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}