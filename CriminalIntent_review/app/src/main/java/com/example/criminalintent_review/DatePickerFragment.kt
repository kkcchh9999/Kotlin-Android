package com.example.criminalintent_review

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"

class DatePickerFragment : DialogFragment() {

    interface Callbacks {   //날짜가 선택되었을 때 함수, 호출하는 부모 액티비티에서 구현
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate : Date = GregorianCalendar(year, month, day).time    //년 월 일 형식의 시간을 .time 으로 date 객체로 얻음
            targetFragment.let { fragment ->    //datePickerFragment 와 연관된 프래그먼트의 인스턴스 참조를 가짐, null 방지를 위해 let 사용
                (fragment as Callbacks).onDateSelected(resultDate)      //CrimeFragment 의 Callbacks 에서 구현한 onDateSelected 를 실행
            }
        }//date Listener, pickerDialog 의 두 번째 인자로 들어갈 예정
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date    //getSerializable 에서 가져온 정보로 설정
        val initialYear = calendar.get(Calendar.YEAR)   //초기 날짜 설정
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(    //DatePickerDialog 실행
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {  //정보 전달을 위해
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}