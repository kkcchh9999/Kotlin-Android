package com.example.recyclerviewpet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val petList = arrayListOf<Pet>(
        Pet("British Shorthair", "Male", "4", "british_shorthair"),
        Pet("Persian Cat", "Male", "8", "persian_cat"),
        Pet("Siamese Cat", "Female", "12", "siamese_cat"),
        Pet("Maine Coon", "Male", "9", "maine_coon"),
        Pet("Ragdoll", "Male", "3", "ragdoll"),
        Pet("Sphynx Cat", "Male", "1", "sphynx_cat"),
        Pet("Abyssinian", "Female", "9", "abyssinian")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_data_list.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rv_data_list.setHasFixedSize(true)  //RecyclerView 사이즈 재구성 방지
     //   rv_data_list.adapter = DataAdapter(dataArray, this)

        //매개변수에 Pet ArrayList 와 람다식
        rv_data_list.adapter = ExtensionDataAdapter(petList, this) {
            Toast.makeText(
                this,
                "Breed: ${it.breed}, Age: ${it.age}",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

}