package com.example.geoquiz_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuiz: TextView
    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton

    private val quizList = listOf(
        Quiz(R.string.quiz_australia, true),
        Quiz(R.string.quiz_oceans, true),
        Quiz(R.string.quiz_mideast, false),
        Quiz(R.string.quiz_africa, false),
        Quiz(R.string.quiz_america, true),
        Quiz(R.string.quiz_asia, true)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //----- findViewById -----
        btnTrue = findViewById(R.id.btn_true)
        btnFalse = findViewById(R.id.btn_false)
        tvQuiz = findViewById(R.id.tv_quiz)
        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)

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

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % quizList.size
            updateUI()
        }

        btnPrevious.setOnClickListener {
            currentIndex = if (currentIndex != 0) {
                (currentIndex - 1) % quizList.size
            } else {
                5
            }
            updateUI()
        }
        updateUI()
    }

    private fun updateUI() {
        tvQuiz.setText(quizList[currentIndex].textResID)
    }
}