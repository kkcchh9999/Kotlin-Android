package com.example.geoquiz_review

import android.app.Activity
import android.content.Intent
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
private const val KEY_INPUT = "input"
private const val KEY_CHEAT = "quiz"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuiz: TextView
    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnCheat: Button
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
        val isCheater = savedInstanceState?.getBoolean(KEY_CHEAT, false) ?: false
        quizViewModel.currentIndex = currentIndex
        quizViewModel.point = point
        quizViewModel.userInput = userInput
        quizViewModel.isCheater = isCheater

        Log.d("이건또 왜이럼", "currentIndex $currentIndex point $point")

        //----- findViewById -----
        btnTrue = findViewById(R.id.btn_true)
        btnFalse = findViewById(R.id.btn_false)
        tvQuiz = findViewById(R.id.tv_quiz)
        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)
        btnCheat = findViewById(R.id.btn_cheat)

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

        btnCheat.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuizAnswer
            val intent = Intent(this, CheatActivity::class.java)
            intent.putExtra("ANSWER", answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)  //onActivityResult 를 위해 requestCode
        }

        updateUI()
    }

    //---- onSaveInstanceState 저장된 상태 -----
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)      //번들 객체로 자신의 상태를 저장하는 부분에 currentIndex 를 추가함.
        savedInstanceState.putInt(KEY_POINT, quizViewModel.point)             //마찬가지 챌린지에서 필요한 요소들도 추가
        savedInstanceState.putDouble(KEY_INPUT, quizViewModel.userInput)
        savedInstanceState.putBoolean(KEY_CHEAT, quizViewModel.isCheater)
    }

    //----- 다른 액티비티의 결과로 진행되는 부분 -----
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
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
        if (quizViewModel.currentQuizAnswer == userAnswer ) {
            if (!quizViewModel.isCheater) {
                Toast.makeText(
                    this,
                    R.string.correct_message,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    R.string.correct_cheated,
                    Toast.LENGTH_SHORT
                ).show()
            }
            quizViewModel.currentQuizIsCorrect = true  //----- chap3 챌린지1 맞춘 문제 true, false 버튼 안보이게하기 -----
            quizViewModel.nextQuiz()
            quizViewModel.point++ //----- chap3 챌린지 2 점수 표기하기 -----
            updateUI()
        } else {
            if (!quizViewModel.isCheater) {
                Toast.makeText(
                    this,
                    R.string.incorrect_message,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    R.string.incorrect_cheated,
                    Toast.LENGTH_SHORT
                ).show()
            }
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