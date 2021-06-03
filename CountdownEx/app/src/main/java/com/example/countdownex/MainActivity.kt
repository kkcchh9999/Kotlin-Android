package com.example.countdownex

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countTask(tv_count, btn_start, btn_stop)    //합성 프로퍼티 리소스 접근
    }
}

fun countTask(count: TextView, start: Button, stop: Button) {
    //Dispatchers.main -> UI 문맥에서 코루틴 실행, 지연 실행을 위해 LAZY
    val job = GlobalScope.launch(Dispatchers.Main, start = CoroutineStart.LAZY){
        for (i in 10 downTo 1) {    //카운트다운
            count.text = "Countdown $i.."   //tv_count 에 텍스트 설정
            delay(1000)
        }
        count.text = "Done!"    //tv_count 에 텍스트 설정
    }
    start.setOnClickListener { job.start() }    //start 버튼 Onclick
    stop.setOnClickListener { job.cancel() }    //stop 버튼 Onclick
}
