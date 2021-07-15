package com.example.criminalintent_review

import java.util.*

//데이터클래스, 범죄에대한 내용
data class Crime(val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false)