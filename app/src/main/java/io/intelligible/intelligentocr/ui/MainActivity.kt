package io.intelligible.intelligentocr.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.intelligible.intelligentocr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)



    }

     fun toPhoneCall(view: View) {
        startActivity(Intent(applicationContext, PhoneCallActivity::class.java))
        finish()
    }
     fun toMeiShi(view: View) {
        startActivity(Intent(applicationContext, MeiShiActivity::class.java))
        finish()
    }
     fun toAddress(view: View) {
        startActivity(Intent(applicationContext, AddressActivity::class.java))
        finish()
    }

}