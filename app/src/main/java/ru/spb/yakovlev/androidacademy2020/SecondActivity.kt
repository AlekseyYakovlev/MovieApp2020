package ru.spb.yakovlev.androidacademy2020

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val textView = findViewById<TextView>(R.id.second_activity_text_view)

        intent.extras?.let {
            val transmittedString = it.getString(TRANSMITTED_STRING, "")
            val transmittedInt = it.getInt(TRANSMITTED_INT, 0)
            val transmittedBoolean = it.getBoolean(TRANSMITTED_BOOLEAN, false)

            textView.text = getString(
                R.string.second_activity_text,
                transmittedString,
                transmittedInt,
                transmittedBoolean
            )
        }
    }

    companion object {
        const val TRANSMITTED_STRING = "transmittedString"
        const val TRANSMITTED_INT = "transmittedInt"
        const val TRANSMITTED_BOOLEAN = "transmittedBoolean"
    }
}