package com.example.geoquiz_review

import androidx.annotation.StringRes

data class Quiz (@StringRes val textResID: Int, val answer: Boolean, var isCorrect: Boolean = false, var isCheated: Boolean = false)