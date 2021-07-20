package com.example.criminalintent_review

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent_review.DB.CrimeDatabase
import com.example.criminalintent_review.DB.migration_1_2
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context){

    //repository 에 데이터베이스와 Dao 생성, 3번째 변수는 DB 파일 이름이고,
    // SQLite 에서는 데이터베이스 하나가 하나의 파일로 생성됨
    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2)  //migration 객체
        .build()
    private val crimeDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    //dao 의 함수들을 이용하기 위해 함수 선언
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(uuid: UUID): LiveData<Crime?> = crimeDao.getCrime(uuid)
    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }
    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    //CrimeRepo 는 싱글톤, 즉 앱 실행중 하나만 생성 -> init 은 최초생성, get 은 기존의 인스턴스 반환
    //private constructor 로 선언하여 init 함수를 호출하지 않고는 인스턴스 생성 불가능
    //따라서 Application 의 서브 클래스를 생성하여 onCreate 에서 init 실행
    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}