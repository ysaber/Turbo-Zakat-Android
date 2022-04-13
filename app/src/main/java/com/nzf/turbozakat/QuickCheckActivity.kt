package com.nzf.turbozakat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nzf.turbozakat.asset.Asset
import kotlinx.android.synthetic.main.activity_quick_check.*

class QuickCheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_check)

        QuickCheckSession.reset()

        val assetAdapter = AssetAdapter(Asset().getAllAssets(this))
        listView.adapter = assetAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == MainActivity.FINISH_ACTIVITY) {
            setResult(MainActivity.FINISH_ACTIVITY)
            finish()
        }
    }
}