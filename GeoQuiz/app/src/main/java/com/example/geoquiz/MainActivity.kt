package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"  //태그
private const val KEY_INDEX = "index"
private const val KEY_CHEATED = "cheat"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton //chap2. 챌린지 2 이전 버튼 만들기
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private var inputCount = 0 //chap3. 챌린지 2   점수 출력하기
    private var point = 0 //chap3. 챌린지 2    점수 출력하기

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)  //화면에 레이아웃을 inflate 하여 나타낸다.

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        val isCheater = savedInstanceState?.getBoolean(KEY_CHEATED, false) ?: false   //chap6. 챌린지 1  화면회전시 데이터 유지하기
        if (isCheater) {
            quizViewModel.setCheated(quizViewModel.currentIndex)     //chap6. 챌린지 1  화면회전시 데이터 유지하기
        }

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button) //chap2. 챌린지 2 이전 버튼 만들기
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {
            inputCount ++
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            inputCount ++
            checkAnswer(false)
        }

        //chap2. 챌린지 1  문제를 눌러도 다음문제 출력하기
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        previousButton.setOnClickListener {     //chap2. 챌린지 2 이전 버튼 만들기
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) {
            return
        }

        if(requestCode == REQUEST_CODE_CHEAT) {
            if (data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) == true) {
                quizViewModel.setCheated(quizViewModel.currentIndex)
            }
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        //Chap3. 챌린지 1 - 정답시 true false 버튼 안보이게하기
        if (quizViewModel.currentQuestionScore) {
            trueButton.visibility = Button.INVISIBLE
            falseButton.visibility = Button.INVISIBLE
        } else {
            trueButton.visibility = Button.VISIBLE
            falseButton.visibility = Button.VISIBLE
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.getCheated(quizViewModel.currentIndex) && userAnswer == correctAnswer -> R.string.judgment_correct_toast
            quizViewModel.getCheated(quizViewModel.currentIndex) && userAnswer != correctAnswer -> R.string.judgment_incorrect_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        if (messageResId == R.string.correct_toast || messageResId == R.string.judgment_correct_toast) {
            quizViewModel.currentQuestionScore = true
            quizViewModel.moveToNext()
            updateQuestion()    //chap3. 챌린지 2  점수 출력하기
            point ++    //chap3. 챌린지 2  점수 출력하기
        }

        if (point == 6) {   
            Toast.makeText(this, "정답률 : "+(6.0/inputCount) * 100.0 + "%", Toast.LENGTH_SHORT).show() //chap3. 챌린지 2 점수 출력하기
        }
        else {
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putBoolean(KEY_CHEATED, quizViewModel.getCheated(quizViewModel.currentIndex)) //chap6. 챌린지 1  화면회전시 데이터 유지하기
    }
}
