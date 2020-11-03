package ru.spb.yakovlev.androidacademy2020

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.first_activity_text_view)

        textView.setOnClickListener { moveToNextScreen() }
    }

    private fun moveToNextScreen() {
        val transmittedString = "String to transmit"
        val transmittedInt = 12
        val transmittedBoolean = true


        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra(SecondActivity.TRANSMITTED_STRING, transmittedString)
            putExtra(SecondActivity.TRANSMITTED_INT, transmittedInt)
            putExtra(SecondActivity.TRANSMITTED_BOOLEAN, transmittedBoolean)
        }

        startActivity(intent)
    }
}