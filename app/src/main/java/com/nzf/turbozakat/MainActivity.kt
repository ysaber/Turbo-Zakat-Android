package com.nzf.turbozakat

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.progressDialog
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        val FINISH_ACTIVITY = 0
    }

    lateinit var downloadingAlert: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences(resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val sharedPrefEditor = sharedPref.edit()
        with(sharedPrefEditor) {
            putBoolean(com.nzf.turbozakat.KEY_NISAB_OPINION_1, true).apply()
            putBoolean(com.nzf.turbozakat.KEY_GOLD_OPINION_1, true).apply()
            putBoolean(com.nzf.turbozakat.KEY_PROPERTY_OPINION_1, false).apply()
            putBoolean(com.nzf.turbozakat.KEY_BUS_INV_OPINION_1, false).apply()
        }

        val lastUpdateLong = sharedPref.getLong("lastupdateday", -1)

        var alreadyUpdatedToday = false

        if (lastUpdateLong > 0) {
            alreadyUpdatedToday = DateUtils.isToday(lastUpdateLong)
        }

        if (isConnected()) {

            if (!alreadyUpdatedToday) {

                sharedPref.edit().putLong("lastupdateday", System.currentTimeMillis()).apply()

                downloadingAlert = progressDialog("Please wait, downloading today's rates...") {
                    setCancelable(false)
                }
                downloadingAlert.show()

                val requestQueue = setupVolley()
                downloadGoldPrices(requestQueue)
            } else {
                pullRatesFromSharedPrefs()
            }
        } else {
            alert("We don't detect an internet connection. Please reconnect to download the latest gold prices and exchange rates.", "Uh oh...") {
                okButton { finish() }
            }.show()
        }


        btnCalculate.setOnClickListener {
            startActivity(Intent(this, CompileAssetListActivity::class.java))
        }

        btnLearn.setOnClickListener {
            startActivity(Intent(this, LearnActivity::class.java))
        }

        btnContact.setOnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun pullRatesFromSharedPrefs() {

        val sharedPref = getSharedPreferences(resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val usdConv = sharedPref.getFloat("USD", 0F)
        val eurConv = sharedPref.getFloat("EUR", 0F)
        val gbpConv = sharedPref.getFloat("GBP", 0F)

//        val cad = Currency(0, "CAD", "$", 1.0)
//        val usd = Currency(1, "USD", "$", usdConv.toDouble())
//        val eur = Currency(2, "EUR", "$", eurConv.toDouble())
//        val gbp = Currency(3, "GBP", "$", gbpConv.toDouble())
//
//        Currencies.currencies.add(cad)
//        Currencies.currencies.add(usd)
//        Currencies.currencies.add(eur)
//        Currencies.currencies.add(gbp)


        Currency.USD.conversionToCad = usdConv.toDouble()
        Currency.EUR.conversionToCad = eurConv.toDouble()
        Currency.GBP.conversionToCad = gbpConv.toDouble()

        val gold = sharedPref.getFloat("gold", 0F)
        val silver = sharedPref.getFloat("silver", 0F)

        GlobalValues.goldPrice = gold.toDouble()
        GlobalValues.silverPrice = silver.toDouble()
        GlobalValues.updateNisabValue(this)
    }


    private fun setupVolley(): RequestQueue {

        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        val network = BasicNetwork(HurlStack())

        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        return requestQueue
    }


    private fun downloadGoldPrices(requestQueue: RequestQueue) {

        downloadingAlert.progress = 25

        val url = "https://nzfcanada.com/wp-content/plugins/nzf-donations/feeds/prices.json"


        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> {
                    GlobalValues.goldPrice = it.getString("gold").toDouble()
                    GlobalValues.silverPrice = it.getString("silver").toDouble()

                    val sharedPref = getSharedPreferences(resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    val sharedPredEditor = sharedPref.edit()
                    sharedPredEditor.putFloat("gold", GlobalValues.goldPrice.toFloat()).apply()
                    sharedPredEditor.putFloat("silver", GlobalValues.silverPrice.toFloat()).apply()

                    GlobalValues.updateNisabValue(this)

                    downloadingAlert.progress = 50

                    downloadCurrencies(requestQueue)
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", error.toString())
                })

        requestQueue.add(request)

    }

    private fun downloadCurrencies(requestQueue: RequestQueue) {

        val url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"

        val simpleRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->

                    downloadingAlert.progress = 75

                    val xmlToJson = XmlToJson.Builder(response).build()
                    val jsonArray: JSONArray = ((((xmlToJson.toJson() as JSONObject).get("gesmes:Envelope") as JSONObject).get("Cube") as JSONObject).get("Cube") as JSONObject).get("Cube") as JSONArray

                    (0 until jsonArray.length())
                            .map { jsonArray[it] }
                            .forEach {

                                val name = (it as JSONObject).getString("currency")
                                val conv = it.getDouble("rate")


                                when (name) {
                                    Currency.CAD.acronym -> {
                                        Currency.EUR.conversionToCad = 1/conv
                                    }
                                    Currency.USD.acronym -> Currency.USD.conversionToCad = conv
                                    Currency.GBP.acronym -> Currency.GBP.conversionToCad = conv
                                }
                            }

                    val divisor = 1/Currency.EUR.conversionToCad

                    Currency.USD.conversionToCad = Currency.USD.conversionToCad / divisor
                    Currency.GBP.conversionToCad = Currency.GBP.conversionToCad / divisor

                    val sharedPref = getSharedPreferences(resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    val sharedPrefEditor = sharedPref.edit()
                    sharedPrefEditor.putFloat( Currency.USD.acronym, Currency.USD.conversionToCad.toFloat()).apply()
                    sharedPrefEditor.putFloat(Currency.EUR.acronym, Currency.EUR.conversionToCad.toFloat()).apply()
                    sharedPrefEditor.putFloat(Currency.GBP.acronym, Currency.GBP.conversionToCad.toFloat()).apply()

                    banner.postDelayed({
                        downloadingAlert.progress = 100
                        downloadingAlert.dismiss()
                    }, 1500)


                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", error.toString())
                })

        requestQueue.add(simpleRequest)
    }


    private fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
