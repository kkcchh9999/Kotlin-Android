package com.example.geoquiz_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuiz: TextView
    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //----- findViewById -----
        btnTrue = findViewById(R.id.btn_true)
        btnFalse = findViewById(R.id.btn_false)


        //----- 이벤트 리스너 -----
        btnTrue.setOnClickListener {
            Toast.makeText(
                this,
                R.string.correct_message,
                Toast.LENGTH_SHORT
            ).show()
        }

        btnFalse.setOnClickListener {
            Toast.makeText(
                this,
                R.string.incorrect_message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}