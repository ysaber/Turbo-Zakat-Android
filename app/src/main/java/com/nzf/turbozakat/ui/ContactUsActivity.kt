//package com.nzf.turbozakat.ui
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.support.v4.app.ActivityCompat
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
//import com.nzf.turbozakat.R
//import kotlinx.android.synthetic.main.activity_contact_us.*
//
//class ContactUsActivity : AppCompatActivity() {
//
//    private val CALL_PERMISSION_REQ_CODE = 123
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_contact_us)
//
//        btnEmailUs.setOnClickListener {
//            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@nzfcanada.com", null))
//            startActivity(Intent.createChooser(emailIntent, "Email us..."))
//        }
//
//        btnWebsite.setOnClickListener {
//            val url = "http://www.nzfcanada.com"
//            val i = Intent(Intent.ACTION_VIEW)
//            i.data = Uri.parse(url)
//            startActivity(i)
//        }
//
//        btnCallUs.setOnClickListener {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION_REQ_CODE)
//            } else {
//                // Permission has already been granted
//                call()
//            }
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            CALL_PERMISSION_REQ_CODE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    call()
//                }
//                return
//            }
//
//            else -> {
//                // Ignore all other requests.
//            }
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private fun call() {
//        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:18886932203"))
//        startActivity(intent)
//    }
//
//}