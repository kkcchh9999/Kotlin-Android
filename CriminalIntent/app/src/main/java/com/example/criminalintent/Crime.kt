package com.example.criminalintent

import java.text.DateFormat
import java.util.*

val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.FULL)    //Chap10 챌린지

data class Crime(
    val id: UUID = UUID.randomUUID(),       //임의의 아이디 값을 생성, 부여
    var title: String = "",
    val date: String = dateFormat.format(Date()),       //Chap10 챌린지
    var isSolved: Boolean = false,
    var requiresPolice: Int = 1 //1이면 police 버튼 필요 X
)