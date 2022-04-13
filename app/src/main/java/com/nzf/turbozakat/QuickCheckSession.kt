package com.nzf.turbozakat

import android.util.Log
import com.nzf.turbozakat.asset.Asset
import java.util.*

object QuickCheckSession {
    val assets: ArrayList<Asset> = ArrayList()
    val completedAssets: ArrayList<Asset> = ArrayList()
    var isCompleted: Boolean = false
    var isFullZakatCheck: Boolean = false

    fun addAsset(asset: Asset) {
        if (!assets.contains(asset)) {
            assets.add(asset)
        }
    }


    fun getLowestIndexedAsset(): Asset {
        if (assets.isEmpty()) throw IndexOutOfBoundsException("No assets found!")
        assets.sortWith(Comparator { asset1, asset2 -> asset1.enum.ordinal - asset2.enum.ordinal })
        return assets[0]
    }


    fun getSecondLowestIndexedAsset(): Asset {
        if (assets.size < 2) throw IndexOutOfBoundsException("No assets found!")
        assets.sortWith(Comparator { asset1, asset2 -> asset1.enum.ordinal - asset2.enum.ordinal })
        return assets[1]
    }


    fun removeLowestIndexedAsset() {
        assets.remove(getLowestIndexedAsset())
    }


    fun reset() {
        assets.clear()
        completedAssets.clear()
        isCompleted = false
        isFullZakatCheck = false
    }

    fun logAllSelectedAssets() {
        assets.forEach {
            Log.d("QuickCheckSession", it.title)
        }
    }


}