package com.example.criminalintent_review.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
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
}