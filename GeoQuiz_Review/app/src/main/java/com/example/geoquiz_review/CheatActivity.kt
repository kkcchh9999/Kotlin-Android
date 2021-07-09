package com.example.geoquiz_review

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz_review.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz_review.answer_shown"
class CheatActivity : AppCompatActivity() {

    private lateinit var btnAnswer: Button
    private lateinit var tvAnswer: TextView
    private var answer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answer = intent.getBooleanExtra("ANSWER", false)

        btnAnswer = findViewById(R.id.btn_answer)
        tvAnswer = findViewById(R.id.tv_answer)

        btnAnswer.setOnClickListener {
            val answerText = when {
                answer -> R.string.btn_true
                else -> R.string.btn_false
            }
            tvAnswer.setText(answerText)

            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isShown)
        }
        setResult(Activity.RESULT_OK, data)

        Log.d("이거 작동하냐?", "여기는될걸?")
    }
}