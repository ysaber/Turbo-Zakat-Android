//package com.nzf.turbozakat.ui
//
//import android.content.Intent
//import android.support.v7.app.AppCompatActivity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.TextView
//import com.nzf.turbozakat.R
//import com.nzf.turbozakat.ui.AssetActivity.Companion.KEY_ASSET
//import com.nzf.turbozakat.ui.AssetActivity.Companion.REQUEST_CODE_NEXT_QUESTION
//import com.nzf.turbozakat.asset.Asset
//
//class AssetAdapter(val assets: List<Asset>) : BaseAdapter() {
//
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.asset_list_item, parent, false)
//        view.findViewById<TextView>(R.id.textView).setText(getItem(position).title)
//        view.setOnClickListener {
//            val intent = Intent(parent.context, AssetActivity::class.java)
//            intent.putExtra(KEY_ASSET, getItem(position))
//            val context = parent.context
//            if (context is AppCompatActivity) {
//                context.startActivityForResult(intent, REQUEST_CODE_NEXT_QUESTION)
//            }
//        }
//        return view
//    }
//
//    override fun getItem(position: Int): Asset {
//        return assets[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getCount(): Int {
//        return assets.size
//    }
//}