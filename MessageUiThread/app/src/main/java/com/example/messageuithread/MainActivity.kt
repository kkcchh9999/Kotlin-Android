package com.example.messageuithread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mHandler: Handler  //핸들러 선언
    lateinit var mThread: Thread    //쓰레드 선언
    private var START = 100         //메세지 구분값(Define 느낌)
    private var COUNT = 101         //동일함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.max = 100

        start_progress.setOnClickListener{  //버튼 클릭 이벤트
            if(!mThread.isAlive){
                mHandler.sendEmptyMessage(START)    //START 메세지 보내기
            }
        }

        mThread = Thread(Runnable {
            for (i in 1..100) {
                Thread.sleep(100)

                val message = Message() //메세지 구성
                message.what = COUNT    //메세지의 종류 = COUNT
                message.arg1 = i        //메세지 값

                mHandler.sendMessage(message)   //COUNT 값 메세지 보내기
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mHandler = MyHandler(this)  //핸들러 생성
    }

    companion object{
        class MyHandler(private val activity: MainActivity) : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == activity.START) {   //메세지가 START 면
                    activity.mThread.start()        //쓰레드 실행

                } else if (msg.what == activity.COUNT) {    //메세지가 COUNT 면
                    activity.progressBar.progress = msg.arg1    //프로그레스바 수치 변경
                    activity.tv_count.text = "Count " + msg.arg1    //COUNT 텍스트 변경
                }

            }
        }
    }
}