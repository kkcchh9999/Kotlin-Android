package com.example.geoquiz_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val KEY_INDEX = "index"
private const val KEY_POINT = "point"
private const val KEY_INPUT = "Input"

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuiz: TextView
    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    //----- 뷰모델 추가하기 -----
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //----- onSaveInstanceState 에서 저장한 내용이 있는지 체크하여 불러오기 -----
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val point = savedInstanceState?.getInt(KEY_POINT, 0) ?: 0
        val userInput = savedInstanceState?.getDouble(KEY_INPUT, 0.0) ?: 0.0
        quizViewModel.currentIndex = currentIndex
        quizViewModel.point = point
        quizViewModel.userInput = userInput


        //----- findViewById -----
        btnTrue = findViewById(R.id.btn_true)
        btnFalse = findViewById(R.id.btn_false)
        tvQuiz = findViewById(R.id.tv_quiz)
        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)

        //----- 이벤트 리스너 -----
        btnTrue.setOnClickListener {
            quizViewModel.userInput++   //----- chap3 챌린지 2 점수 표기하기 -----
            checkAnswer(true)
        }

        btnFalse.setOnClickListener {
            quizViewModel.userInput++ //----- chap3 챌린지 2 점수 표기하기 -----
            checkAnswer(false)
        }

        btnNext.setOnClickListener {
            quizViewModel.nextQuiz()
            updateUI()
        }

        btnPrevious.setOnClickListener {
            quizViewModel.previousQuiz()
            updateUI()
        }
        updateUI()
    }

    //---- onSaveInstanceState 저장된 상태 -----
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)      //번들 객체로 자신의 상태를 저장하는 부분에 currentIndex 를 추가함.
        outState.putInt(KEY_POINT, quizViewModel.point)             //마찬가지 챌린지에서 필요한 요소들도 추가
        outState.putDouble(KEY_INPUT, quizViewModel.userInput)
    }

    //----- UI 업데이트 -----
    private fun updateUI() {
        tvQuiz.setText(quizViewModel.currentQuiz)
        //----- chap3 챌린지1 맞춘 문제 true, false 버튼 안보이게하기 -----
        if (quizViewModel.currentQuizIsCorrect) {
            btnTrue.visibility = View.INVISIBLE
            btnFalse.visibility = View.INVISIBLE
        } else {
            btnTrue.visibility = View.VISIBLE
            btnFalse.visibility = View.VISIBLE
        }
    }

    private fun checkAnswer (userAnswer: Boolean) {
        if (quizViewModel.currentQuizAnswer == userAnswer) {
            Toast.makeText(
                this,
                R.string.correct_message,
                Toast.LENGTH_SHORT
            ).show()
            quizViewModel.currentQuizIsCorrect = true  //----- chap3 챌린지1 맞춘 문제 true, false 버튼 안보이게하기 -----
            quizViewModel.nextQuiz()
            quizViewModel.point++ //----- chap3 챌린지 2 점수 표기하기 -----
            updateUI()
        } else {
            Toast.makeText(
                this,
                R.string.incorrect_message,
                Toast.LENGTH_SHORT
            ).show()
        }
        //----- chap3 챌린지 2 점수 표기하기 -----
        if (quizViewModel.point == quizViewModel.quizListSize) {
            val score = quizViewModel.returnScore()
            Toast.makeText(
                this,
                "정답률: $score%",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}