package com.example.geoquiz_review

import android.text.BoringLayout
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val quizList = listOf(
        Quiz(R.string.quiz_australia, true),
        Quiz(R.string.quiz_oceans, true),
        Quiz(R.string.quiz_mideast, false),
        Quiz(R.string.quiz_africa, false),
        Quiz(R.string.quiz_america, true),
        Quiz(R.string.quiz_asia, true)
    )
    var currentIndex = 0
    var userInput = 0.0    //----- chap3 챌린지 2 점수 표기하기 -----
    var point = 0    //----- chap3 챌린지 2 점수 표기하기 -----
    var cheatCount = 0  //----- chap7 챌린지 커닝 3회 제한하기 -----

    val currentQuizAnswer: Boolean
        get() = quizList[currentIndex].answer

    val currentQuiz: Int
        get() = quizList[currentIndex].textResID

    val quizListSize: Int
        get() = quizList.size

    var currentQuizIsCorrect: Boolean       //chap6 챌린지, 커닝 여부 각각 설정하기
        get() = quizList[currentIndex].isCorrect
        set(input: Boolean) {
            quizList[currentIndex].isCorrect = input
        }

    var isCheater: Boolean
        get() = quizList[currentIndex].isCheated
        set(value: Boolean) {
            quizList[currentIndex].isCheated = value
        }
    fun nextQuiz() {
        currentIndex = (currentIndex + 1) % quizList.size
    }

    fun previousQuiz() {
        currentIndex = if (currentIndex != 0) {
            (currentIndex - 1) % quizList.size
        } else {
            5
        }
    }

    fun returnScore(): Double {
        return point / userInput * 100
    }

}