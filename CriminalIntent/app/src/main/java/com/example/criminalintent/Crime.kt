package com.example.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity //하나의 데이터베이스의 테이블임을 의미
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),       //임의의 아이디 값을 생성, 부여, primaryKey 설정
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false,
                 var suspect: String = "") {

    val photoFileName
        get() = "IMG_$id.jpg"
}