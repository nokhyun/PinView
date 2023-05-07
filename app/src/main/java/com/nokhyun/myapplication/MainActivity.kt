package com.nokhyun.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nokhyun.pinview.PinView

class MainActivity : AppCompatActivity() {
    private val tvPinState: TextView by lazy { findViewById(R.id.tvPinState) }
    private val btnPinState: Button by lazy { findViewById(R.id.btnPinState) }
    private val pinView: PinView by lazy { findViewById(R.id.pinView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPinState.setOnClickListener {
            tvPinState.text = pinView.pinState("test")
        }

        pinView.apply {
            initPinCodeSetting("testPinCode")
            savePinCode("test", listOf("1", "1", "1", "1"))

            onSuccess = {
                Toast.makeText(applicationContext, "onSuccess", Toast.LENGTH_SHORT).show()
            }

            onFailure = {
                Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}