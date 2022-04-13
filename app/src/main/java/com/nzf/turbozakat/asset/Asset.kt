package com.nzf.turbozakat.asset

import android.content.Context
import com.nzf.turbozakat.*
import com.nzf.turbozakat.equationparams.CashParams
import com.nzf.turbozakat.equationparams.EquationParams
import java.io.Serializable

val ZAKAT_RATIO = 0.025

data class Asset(val enum: AssetEnum = AssetEnum.NONE, var title: String = "", var initialQuestion: Question? = null, var equationParams: EquationParams = CashParams(),
                 var calculatedAmount: Double = 0.0,
                 val questions: ArrayList<Question?> = arrayListOf()) : Serializable {

    fun getAllAssets(context: Context): ArrayList<Asset> {
        val sharedPref = context.getSharedPreferences(context.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val isGoldOpinion1 = sharedPref.getBoolean(KEY_GOLD_OPINION_1, true)
        val isPropertyOpinion1 = sharedPref.getBoolean(KEY_PROPERTY_OPINION_1, false)
        val isBusInvOpinion1 = sharedPref.getBoolean(KEY_BUS_INV_OPINION_1, false)

        return arrayListOf(
                CashAsset().cash,
                PropertyAsset(isPropertyOpinion1).property,
                GoldAsset(isGoldOpinion1).gold,
                InvestmentAsset().investment,
                BusinessInvestmentAsset(isBusInvOpinion1).businessInvestment,
                SavingsPlanAsset().savingsPlan,
                Liabilities().liabilities)
    }

    override fun equals(other: Any?): Boolean {
        return other is Asset && title == other.title
    }

    fun getAssetByEnum(context: Context, assetEnum: AssetEnum): Asset? {
        getAllAssets(context).forEach {
            if (it.enum == assetEnum) {
                return it
            }
        }
        return null
    }

    fun isType(enumAsset: AssetEnum) = enum == enumAsset
}

enum class AssetEnum {
    NONE, CASH, PROPERTY, GOLD, INVESTMENT, BUSINESS, SAVINGSPLAN, LIABILITIES
}
