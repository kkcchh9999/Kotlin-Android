package com.example.helloandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var mWelcomeTextView: TextView //지연 초기화를 위한 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //메인 액티비티

        mWelcomeTextView = findViewById(R.id.tv_hello)  //지연 초기화 시점
    }
}