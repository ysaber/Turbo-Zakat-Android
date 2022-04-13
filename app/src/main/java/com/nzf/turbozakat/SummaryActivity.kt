package com.nzf.turbozakat

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.nzf.turbozakat.apwlibrary.PDFWriter
import com.nzf.turbozakat.apwlibrary.PaperSize
import com.nzf.turbozakat.apwlibrary.StandardFonts
import com.nzf.turbozakat.asset.ZAKAT_RATIO
import kotlinx.android.synthetic.main.activity_quick_check_summary.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.yesButton
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class SummaryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_check_summary)

        var total = 0.0

        if (QuickCheckSession.isFullZakatCheck) {
            QuickCheckSession.completedAssets.forEach {
                total += it.calculatedAmount
            }
        } else {
            QuickCheckSession.assets.forEach {
                total += it.calculatedAmount
            }
        }

        val isAboveNisab = total >= GlobalValues.nisabValue

        val zakatableAmount = if (isAboveNisab) total else 0.0
        val zakatAmount = zakatableAmount * ZAKAT_RATIO

        amountOwing.text = "TOTAL AMOUNT OWING:\n($${zakatableAmount.format(2)} x 2.5%)"
        totalAmountOwing.text = "$${zakatAmount.format(2)}"

        amount.text = "Zakatable amount = $${total.format(2)}"

        nisab_note.text = if (isAboveNisab) {
            "* Nisab amount: $${GlobalValues.nisabValue.format(2)}"
        } else {
            "* Your total zakatable amount ($${total.format(2)}) is below the nisab level ($${GlobalValues.nisabValue.format(2)}). Therefore, you don't owe any zakat on your assets."
        }



        listView.adapter = QuickCheckSummaryAdapter()


        btnDonate.setOnClickListener {

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Zakat Amount", zakatAmount.format(2))
            clipboard.primaryClip = clip
            clipboard.text = clip.toString()

            Toast.makeText(this@SummaryActivity, "Amount owing copied to clipboard!", Toast.LENGTH_LONG).show()

            val uris = Uri.parse("https://nzfcanada.com/donate/")
            val intents = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intents)
        }


        btnContact.setOnClickListener {
            QuickCheckSession.reset()
            startActivity(Intent(this, ContactUsActivity::class.java))
        }

        btnClose.setOnClickListener {
            onBackPressed()
        }

        btnPdf.setOnClickListener {
            createPdf()
        }
    }


    override fun onBackPressed() {
        alert("If you close this screen, you will lose this information!", "Are you sure?") {
            yesButton {
                QuickCheckSession.reset()
                finish()
            }
            noButton { it.dismiss() }
        }.show()
    }


    private fun createPdf() {

        if (isStoragePermissionGranted()) {
            writeToPdf()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            writeToPdf()
        }
    }


    private fun writeToPdf() {
        val writer = PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT)
        writer.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN, StandardFonts.WIN_ANSI_ENCODING)
        writer.addText(20, 900, 14, "Your zakat calculation, completed using the NZF Turbo Zakat app:")

        writer.addText(20, 850, 14, "Asset")
        writer.addText(350, 850, 14, "Zakatable amount")
        writer.addText(500, 850, 14, "Zakat owing")
        writer.addText(20, 845, 14, "________________________________________________________________________________")

        QuickCheckSession.completedAssets.forEachIndexed { index, asset ->

            val string = "${(index + 1)}. ${asset.equationParams.label} (${asset.title}): "
            writer.addText(20, 800 - (index * 50), 14, string)

            val string2 = if (asset.calculatedAmount < 0) {
                "$(${Math.abs(asset.calculatedAmount).format(2)})"
            } else {
                "$${asset.calculatedAmount.format(2)}"
            }
            writer.addText(350, 800 - (index * 50), 14, string2)

            val string3 = if ((asset.calculatedAmount * ZAKAT_RATIO) < 0) {
                "$(${(Math.abs(asset.calculatedAmount * ZAKAT_RATIO)).format(2)})"
            } else {
                "$${(asset.calculatedAmount * ZAKAT_RATIO).format(2)}"
            }
            writer.addText(500, 800 - (index * 50), 14, string3)
        }


        var totalZakatable = 0.0

        if (QuickCheckSession.isFullZakatCheck) {
            QuickCheckSession.completedAssets.forEach {
                totalZakatable += it.calculatedAmount
            }
        } else {
            QuickCheckSession.assets.forEach {
                totalZakatable += it.calculatedAmount
            }
        }

        writer.addText(20, 800 - ((QuickCheckSession.completedAssets.size * 50)), 14, "________________________________________________________________________________")

        val stringTotalZakatable = "$${(totalZakatable).format(2)}"
        writer.addText(350, 800 - ((QuickCheckSession.completedAssets.size * 50) + 25), 14, stringTotalZakatable)

        val stringTotalZakatOwing = "$${(totalZakatable * ZAKAT_RATIO).format(2)}"
        writer.addText(500, 800 - ((QuickCheckSession.completedAssets.size * 50) + 25), 14, stringTotalZakatOwing)

        outputToFile(writer.asString(), "ISO-8859-1")
    }


    private fun outputToFile(pdfContent: String, encoding: String) {
        var newFile = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}" + "/TurboTax Summary.pdf")
        try {

            for (i in 1..999) {
                if (newFile.exists()) {
                    newFile = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}" + "/TurboTax Summary" + " (" + i + ")" + ".pdf")
                } else {
                    break
                }
            }

            newFile.createNewFile()
            try {
                val pdfFile = FileOutputStream(newFile)



                pdfFile.write(pdfContent.toByteArray(charset(encoding)))
                pdfFile.close()

                alert("The PDF has been saved to your phone's Downloads folder") {
                    okButton { it.dismiss() }
                }.show()

            } catch (e: FileNotFoundException) {
                Log.d("sdfsf", "Error: " + e.localizedMessage)
            }

        } catch (e: IOException) {
            Log.d("sdfsf", "Error: " + e.localizedMessage)
        }

    }


    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else {
            true
        }
    }
}
