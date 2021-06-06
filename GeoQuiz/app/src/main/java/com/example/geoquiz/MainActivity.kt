package com.example.geoquiz

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

private const val TAG = "MainActivity"  //태그

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton //chap2. 챌린지 2 이전 버튼 만들기
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var inputCount = 0 //chap3. 챌린지 2   점수 출력하기
    private var point = 0 //chap3. 챌린지 2    점수 출력하기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)  //화면에 레이아웃을 inflate 하여 나타낸다.

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button) //chap2. 챌린지 2 이전 버튼 만들기
        questionTextView = findViewById(R.id.question_text_view)

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
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        previousButton.setOnClickListener {     //chap2. 챌린지 2 이전 버튼 만들기
            if (currentIndex == 0) {
                currentIndex = questionBank.size - 1
                updateQuestion()
            }
            else {
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
            }
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        //Chap3. 챌린지 1 - 정답시 true false 버튼 안보이게하기
        if (questionBank[currentIndex].score) {
            trueButton.visibility = Button.INVISIBLE
            falseButton.visibility = Button.INVISIBLE
        } else {
            trueButton.visibility = Button.VISIBLE
            falseButton.visibility = Button.VISIBLE
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        if (messageResId == R.string.correct_toast) {
            questionBank[currentIndex].score = true
            currentIndex = (currentIndex + 1) % questionBank.size
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
}
