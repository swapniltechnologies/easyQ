package com.example.easyq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_code.*

class CodeActivity : AppCompatActivity() {

    lateinit var editText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)


        enter.setOnClickListener {

            editText = findViewById(R.id.editText) as EditText
            val time = editText.text.toString()

            val intent = Intent(this@CodeActivity,counter::class.java)
            intent.putExtra("Time",time)
            startActivity(intent)

        }

    }

}
