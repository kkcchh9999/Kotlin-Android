package com.example.criminalintent_review.DB

import androidx.room.TypeConverter
import java.util.*

class CrimeTypeConverters {

    //DB 에서 모르는 Date 형식을 변환해주는 컨버터
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    //DB 에서 모르는 UUID 형식을 변환해주는 컨버터
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
}