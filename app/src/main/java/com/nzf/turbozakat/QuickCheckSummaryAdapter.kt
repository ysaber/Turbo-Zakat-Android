package com.nzf.turbozakat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nzf.turbozakat.asset.Asset
import kotlinx.android.synthetic.main.summary_item.view.*

class QuickCheckSummaryAdapter(private val assets: ArrayList<Asset> = if (QuickCheckSession.isFullZakatCheck) QuickCheckSession.completedAssets else QuickCheckSession.assets): BaseAdapter() {

    override fun getItem(position: Int) = assets[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = assets.size


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.summary_item, parent, false)

        val asset = getItem(position)

        view.name.text = "${asset.equationParams.label} (${asset.title})"
        view.amount.text = if (asset.calculatedAmount < 0) {
            "$(${Math.abs(asset.calculatedAmount).format(2)})"
        } else {
            "$${asset.calculatedAmount.format(2)}"
        }

        return view
    }

}