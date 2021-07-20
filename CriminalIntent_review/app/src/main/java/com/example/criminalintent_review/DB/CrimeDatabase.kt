package com.example.criminalintent_review.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.criminalintent_review.Crime

@Database(entities = [Crime::class], version = 2)
@TypeConverters(CrimeTypeConverters::class)  //TypeConverters 클래스를 참조하여 타입컨버터 적용
abstract class CrimeDatabase : RoomDatabase() {
    //DB가 생성되면 Dao 인터페이스를 Room 이 구현 따라서 Dao 의 함수 사용 가능
    abstract fun crimeDao(): CrimeDataAccessObject
}

val migration_1_2 = object : Migration(1, 2) {  //업데이트 이전 버전, 이후 버전을 인자로 받음
    override fun migrate(database: SupportSQLiteDatabase) { //이 함수에서는 버전을 업데이트하기 위해 필요한 쿼리문 작성
        database.execSQL(                                   //이후 CrimeRepository 에 migration 객체를 제공하게 변경
            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}