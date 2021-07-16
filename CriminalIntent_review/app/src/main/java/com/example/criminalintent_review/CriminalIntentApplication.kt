package com.example.criminalintent_review

import android.app.Application

class CriminalIntentApplication : Application() {
    //해당 부분은 앱이 처음 시작하거나, 메모리에서 제거된 후 다시 실행될 때 한번만 작동 따라서 초기화 작업 수행에 용이
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.init(this)
    }
}