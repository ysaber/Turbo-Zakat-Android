package com.nzf.turbozakat

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_settings.*

const val KEY_NISAB_OPINION_1 = "KEY_NISAB_OPINION_1"
const val KEY_GOLD_OPINION_1 = "KEY_GOLD_OPINION_1"
const val KEY_PROPERTY_OPINION_1 = "KEY_PROPERTY_OPINION_1"
const val KEY_BUS_INV_OPINION_1 = "KEY_BUS_INV_OPINION_1"

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = getSharedPreferences(resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        initNisab(sharedPref)
        initGold(sharedPref)
        initProperty(sharedPref)
        initBusinessInvestments(sharedPref)
        initRates()
    }


    private fun initNisab(sharedPref: SharedPreferences) {
        val sharedPrefEditor = sharedPref.edit()
        val nisabOpinion1 = sharedPref.getBoolean(KEY_NISAB_OPINION_1, true)

        nisab_opinion_1_checkbox.isChecked = nisabOpinion1
        nisab_opinion_2_checkbox.isChecked = !nisabOpinion1

        val busInvOpinion2ClickListener = View.OnClickListener {
            nisab_opinion_1_checkbox.isChecked = false
            nisab_opinion_2_checkbox.isChecked = true
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_NISAB_OPINION_1, false).apply()
                GlobalValues.updateNisabValue(this@SettingsActivity)
            }
        }

        val busInvOpinion1ClickListener = View.OnClickListener {
            nisab_opinion_1_checkbox.isChecked = true
            nisab_opinion_2_checkbox.isChecked = false
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_NISAB_OPINION_1, true).apply()
                GlobalValues.updateNisabValue(this@SettingsActivity)
            }
        }

        nisab_opinion_2_container.setOnClickListener(busInvOpinion2ClickListener)
        nisab_opinion_2_checkbox.setOnClickListener(busInvOpinion2ClickListener)

        nisab_opinion_1_container.setOnClickListener(busInvOpinion1ClickListener)
        nisab_opinion_1_checkbox.setOnClickListener(busInvOpinion1ClickListener)
    }


    private fun initGold(sharedPref: SharedPreferences) {
        val sharedPrefEditor = sharedPref.edit()
        val goldOpinion1 = sharedPref.getBoolean(KEY_GOLD_OPINION_1, true)

        gold_silver_opinion_1_checkbox.isChecked = goldOpinion1
        gold_silver_opinion_2_checkbox.isChecked = !goldOpinion1

        val goldSilverOpinion2ClickListener = View.OnClickListener {
            gold_silver_opinion_1_checkbox.isChecked = false
            gold_silver_opinion_2_checkbox.isChecked = true
            with(sharedPrefEditor) {
                putBoolean(KEY_GOLD_OPINION_1, false).apply()
            }
        }


        val goldSilverOpinion1ClickListener = View.OnClickListener {
            gold_silver_opinion_1_checkbox.isChecked = true
            gold_silver_opinion_2_checkbox.isChecked = false
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_GOLD_OPINION_1, true).apply()
            }
        }

        gold_silver_opinion_2_container.setOnClickListener(goldSilverOpinion2ClickListener)
        gold_silver_opinion_2_checkbox.setOnClickListener(goldSilverOpinion2ClickListener)

        gold_silver_opinion_1_container.setOnClickListener(goldSilverOpinion1ClickListener)
        gold_silver_opinion_1_checkbox.setOnClickListener(goldSilverOpinion1ClickListener)
    }


    private fun initProperty(sharedPref: SharedPreferences) {
        val sharedPrefEditor = sharedPref.edit()
        val propertyOpinion1 = sharedPref.getBoolean(KEY_PROPERTY_OPINION_1, false)

        property_opinion_1_checkbox.isChecked = propertyOpinion1
        property_opinion_2_checkbox.isChecked = !propertyOpinion1

        val propertyOpinion1ClickListener = View.OnClickListener {
            property_opinion_1_checkbox.isChecked = true
            property_opinion_2_checkbox.isChecked = false
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_PROPERTY_OPINION_1, true).apply()
            }
        }

        val propertyOpinion2ClickListener = View.OnClickListener {
            property_opinion_1_checkbox.isChecked = false
            property_opinion_2_checkbox.isChecked = true
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_PROPERTY_OPINION_1, false).apply()
            }
        }
        property_opinion_2_container.setOnClickListener(propertyOpinion2ClickListener)
        property_opinion_2_checkbox.setOnClickListener(propertyOpinion2ClickListener)

        property_opinion_1_container.setOnClickListener(propertyOpinion1ClickListener)
        property_opinion_1_checkbox.setOnClickListener(propertyOpinion1ClickListener)
    }


    private fun initBusinessInvestments(sharedPref: SharedPreferences) {
        val sharedPrefEditor = sharedPref.edit()
        val busInvOpinion1 = sharedPref.getBoolean(KEY_BUS_INV_OPINION_1, false)

        bus_inv_opinion_1_checkbox.isChecked = busInvOpinion1
        bus_inv_opinion_2_checkbox.isChecked = !busInvOpinion1


        val busInvOpinion2ClickListener = View.OnClickListener {
            bus_inv_opinion_1_checkbox.isChecked = false
            bus_inv_opinion_2_checkbox.isChecked = true
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_BUS_INV_OPINION_1, false).apply()
            }
        }


        val busInvOpinion1ClickListener = View.OnClickListener {
            bus_inv_opinion_1_checkbox.isChecked = true
            bus_inv_opinion_2_checkbox.isChecked = false
            with(sharedPrefEditor) {
                putBoolean(com.nzf.turbozakat.KEY_BUS_INV_OPINION_1, true).apply()
            }
        }

        bus_inv_opinion_2_container.setOnClickListener(busInvOpinion2ClickListener)
        bus_inv_opinion_2_checkbox.setOnClickListener(busInvOpinion2ClickListener)

        bus_inv_opinion_1_container.setOnClickListener(busInvOpinion1ClickListener)
        bus_inv_opinion_1_checkbox.setOnClickListener(busInvOpinion1ClickListener)
    }


    @SuppressLint("SetTextI18n")
    private fun initRates() {
        rates_gold.text = "Gold: CAD $${GlobalValues.goldPrice.format(2)} /gram"
        rates_silver.text = "Silver: CAD $${GlobalValues.silverPrice.format(2)} /gram"
        rates_usd.text = "CAD $1 = $${Currency.USD.conversionToCad.format(2)} USD"
        rates_eur.text = "CAD $1 = €${Currency.EUR.conversionToCad.format(2)} EUR"
        rates_gbp.text = "CAD $1 = £${Currency.GBP.conversionToCad.format(2)} GBP"
    }
}