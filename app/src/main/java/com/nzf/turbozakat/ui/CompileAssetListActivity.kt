//package com.nzf.turbozakat.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.widget.Toast
//import com.nzf.turbozakat.QuickCheckSession
//import com.nzf.turbozakat.R
//import com.nzf.turbozakat.SelectAssetsAdapter
//import com.nzf.turbozakat.asset.Asset
//import kotlinx.android.synthetic.main.activity_compile_assets.*
//
//class CompileAssetListActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_compile_assets)
//        listView.adapter = SelectAssetsAdapter(Asset().getAllAssets(this))
//
//        QuickCheckSession.reset()
//
//        getStartedBtn.setOnClickListener {
//
//            if (QuickCheckSession.assets.size == 0) {
//                Toast.makeText(this, "Please select at least 1 asset", Toast.LENGTH_LONG).show()
//            } else {
//                QuickCheckSession.isFullZakatCheck = true
//
//                var assets = arrayListOf<Asset>()
//                assets.addAll(QuickCheckSession.assets)
//
//                QuickCheckSession.assets.clear()
//
//                assets.forEach {
//                    QuickCheckSession.assets.add(Asset().getAssetByEnum(this, it.enum)!!)
//                }
//
//                val intent = Intent(this, AssetActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//    }
//
//}