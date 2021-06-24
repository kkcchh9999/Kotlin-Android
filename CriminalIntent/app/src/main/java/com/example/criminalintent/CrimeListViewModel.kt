package com.example.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {

    val crimes = mutableListOf<Crime>() //장기간 저장할 수 는 없지만, ViewModel 에서 뷰를 채우는데 필요한 데이터를 가짐

    init {
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i%3 == 0
            if (i % 2 == 0) crime.requiresPolice = 2    //2이면 police 버튼 생성
            crimes += crime
        }
    }

}