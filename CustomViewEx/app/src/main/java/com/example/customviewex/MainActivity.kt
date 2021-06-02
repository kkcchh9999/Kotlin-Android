package com.example.customviewex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    val person = Person("사람", 12)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, SubActivity::class.java)
        var bundle = Bundle()
        bundle.putParcelable("selected_person", person)
        intent.putExtra("myBundle",bundle)
        startActivity(intent)
    }
}