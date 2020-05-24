package com.example.easyq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_code.*
import java.sql.Time

class counter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        val time = intent.getStringExtra("Time")
        val timedisplay = findViewById<TextView>(R.id.time_text)
        timedisplay.text = "Est. Time Remaining : "+time

    }
}
