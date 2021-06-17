package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var cheatCount = 0      //chap7. 챌린지 2 치트 3회만 할 수 있게

    var cheated = mutableListOf(false, false, false, false, false, false) //chap6. 챌린지 2 요소별 치트 여부 확인

    fun getCheated(index: Int): Boolean {   //chap6. 챌린지 2 요소별 치트 여부 확인
        return cheated[index]
    }

    fun setCheated(index: Int) {    //chap6. 챌린지 2 요소별 치트 여부 확인
        cheated[index] = true
    }

    fun plusCheat() { //chap7. 챌린지 2 치트 3회만 할 수 있게
        cheatCount ++
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var currentQuestionScore: Boolean
        get() = questionBank[currentIndex].score
        set(value) { questionBank[currentIndex].score = true }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        if (currentIndex == 0) {
            currentIndex = questionBank.size - 1
        }
        else {
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }

    override fun onCleared() {  //소멸되기 전 생명주기
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}