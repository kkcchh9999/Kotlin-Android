package com.example.criminalintent

import java.util.*

data class Crime(
    val id: UUID = UUID.randomUUID(),       //임의의 아이디 값을 생성, 부여
    var title: String = "",
    val date: Date = Date(),
    var isSolved: Boolean = false
)