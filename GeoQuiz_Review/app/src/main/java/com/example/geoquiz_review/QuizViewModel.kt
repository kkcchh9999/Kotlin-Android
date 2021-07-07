package com.example.geoquiz_review

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

    val currentQuizAnswer: Boolean
        get() = quizList[currentIndex].answer

    val currentQuiz: Int
        get() = quizList[currentIndex].textResID

    val quizListSize: Int
        get() = quizList.size

    var currentQuizIsCorrect: Boolean
        get() = quizList[currentIndex].isCorrect
        set(input: Boolean) {
            quizList[currentIndex].isCorrect = input
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