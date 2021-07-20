package com.example.criminalintent_review

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//데이터클래스, 범죄에대한 내용 -> Entity 로 테이블 구조로 변경
@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var suspect: String = "",
                 var isSolved: Boolean = false)