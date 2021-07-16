package com.example.criminalintent_review.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent_review.Crime

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)  //TypeConverters 클래스를 참조하여 타입컨버터 적용
abstract class CrimeDatabase : RoomDatabase() {
    //DB가 생성되면 Dao 인터페이스를 Room 이 구현 따라서 Dao 의 함수 사용 가능
    abstract fun crimeDao(): CrimeDataAccessObject
}