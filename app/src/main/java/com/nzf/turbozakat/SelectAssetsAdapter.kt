package com.nzf.turbozakat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nzf.turbozakat.asset.Asset
import com.nzf.turbozakat.asset.CashAsset
import kotlinx.android.synthetic.main.select_asset_line_item.view.*

class SelectAssetsAdapter(val assets: List<Asset>): BaseAdapter() {

    override fun getItem(position: Int): Asset = assets[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = assets.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val asset = getItem(position)

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.select_asset_line_item, parent, false)

        if (asset == CashAsset().cash) {
            view.isSelected = true
            view.checkbox.setImageResource(R.drawable.ic_checkbox_full)
            QuickCheckSession.addAsset(asset)
        }

        view.textView.text = asset.title

        view.cardView.setOnClickListener {
            view.isSelected = !view.isSelected
            view.checkbox.setImageResource(if (view.isSelected) R.drawable.ic_checkbox_full else R.drawable.ic_checkbox_empty)
            if (view.isSelected) {
                QuickCheckSession.addAsset(asset)
            } else {
                QuickCheckSession.assets.remove(asset)
            }
            QuickCheckSession.logAllSelectedAssets()
        }

        return view
    }
}