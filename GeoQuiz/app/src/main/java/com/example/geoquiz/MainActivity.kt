package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  //화면에 레이아웃을 inflate 하여 나타낸다.

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        trueButton.setOnClickListener {
            var myToast = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.CENTER, 0, 0)
            println(myToast.gravity)    //sdk30 이후로 더이상 toast 메세지의 gravity 편집 불가능
            myToast.show()
        }

        falseButton.setOnClickListener { view: View ->
            Toast.makeText(
                this,
                R.string.incorrect_toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}