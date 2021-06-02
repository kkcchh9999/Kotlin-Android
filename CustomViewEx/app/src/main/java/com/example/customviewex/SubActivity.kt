package com.example.customviewex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val bundle = intent.getBundleExtra("myBundle")
        var person = bundle?.getParcelable<Person>("selected_person") as Person
    }
}