package com.example.criminalintent_review.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.criminalintent_review.Crime
import java.util.*

@Dao
interface CrimeDataAccessObject {

    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>      //getCrimes 함수는 위의 쿼리를 실행, List<Crime> 를 반환
                                                //LiveData 로 변경하여 백그라운드 스레드에서 실행

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>    //getCrime 함수에서 id를 참조해서 위의 쿼리를 실행, Crime 객체 반환
                                                //LiveData 로 변경하여 백그라운드 스레드에서 실행

    @Update //Update, Insert 는 쿼리문을 설정하지 않아도 Room 에서 적절한 쿼리 생성해줌.
    fun updateCrime(crime: Crime)

    @Insert
    fun addCrime(crime: Crime)
}