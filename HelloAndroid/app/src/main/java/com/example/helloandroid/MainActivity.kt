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

/*
제어문 옆에는 소괄호 사용시 공백을 둠
while (true) {
    toDo()
}

비어 있는 블록 구성시에도 중괄호는 내려서 씀
if (true) {
}

예약된 키워드에 괄호가 사용되는 경우 공백 같이 사용
for(i in 0..1) {
} -> 틀림
for (i in 0..1) {
} -> 좋음

중괄호 사용시, 이진 연산자 사용시 공백 추가
if (true){
} -> 틀림
if (true) {
} -> 좋음

val two = 1+1 -> 틀림
val two = 1 + 1 -> 좋음
 */