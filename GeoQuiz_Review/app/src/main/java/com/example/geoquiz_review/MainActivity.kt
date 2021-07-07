package com.example.geoquiz_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var userInputCount = 0.0    //----- chap3 챌린지 2 점수 표기하기 -----
    private var userCorrectCount = 0    //----- chap3 챌린지 2 점수 표기하기 -----

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
            userInputCount ++   //----- chap3 챌린지 2 점수 표기하기 -----
            checkAnswer(true)
        }

        btnFalse.setOnClickListener {
            userInputCount ++   //----- chap3 챌린지 2 점수 표기하기 -----
            checkAnswer(false)
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

    //----- UI 업데이트 -----
    private fun updateUI() {
        tvQuiz.setText(quizList[currentIndex].textResID)
        //----- chap3 챌린지1 맞춘 문제 true, false 버튼 안보이게하기 -----
        if (quizList[currentIndex].correctCount) {
            btnTrue.visibility = View.INVISIBLE
            btnFalse.visibility = View.INVISIBLE
        } else {
            btnTrue.visibility = View.VISIBLE
            btnFalse.visibility = View.VISIBLE
        }
    }

    private fun checkAnswer (userAnswer: Boolean) {
        if (quizList[currentIndex].answer == userAnswer) {
            Toast.makeText(
                this,
                R.string.correct_message,
                Toast.LENGTH_SHORT
            ).show()
            quizList[currentIndex].correctCount = true  //----- chap3 챌린지1 맞춘 문제 true, false 버튼 안보이게하기 -----
            currentIndex = (currentIndex + 1) % quizList.size
            userCorrectCount ++ //----- chap3 챌린지 2 점수 표기하기 -----
            updateUI()
        } else {
            Toast.makeText(
                this,
                R.string.incorrect_message,
                Toast.LENGTH_SHORT
            ).show()
        }
        //----- chap3 챌린지 2 점수 표기하기 -----
        if (userCorrectCount == quizList.size) {
            Toast.makeText(
                this,
                "정답률: " + userCorrectCount / userInputCount * 100 + "%",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("이게무슨일이야", "정답카운터"+userCorrectCount+"인풋카운터"+userInputCount)
        }
    }
}