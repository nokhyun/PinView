package com.nokhyun.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.nokhyun.pinview.PinView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<PinView>(R.id.pinView).apply {
            initPinCodeSetting("testPinCode")
            savePinCode("test", listOf("1","1","1","1"))

            onSuccess =  {
                Toast.makeText(applicationContext, "onSuccess", Toast.LENGTH_SHORT).show()
            }

            onFailure = {
                Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}