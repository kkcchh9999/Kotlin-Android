package com.example.criminalintent_review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //fragment 에서는 view 를 inflate 하지 않는다 = setContentView(R.layout.activity_main) 선언 X
        //view 는 onCreateView 에서 구성
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        dateButton.apply {
            text = crime.date.toString()    //버튼의 텍스트는 날짜
            isEnabled = false               //버튼 비활성화
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //해당 부분은 구현 필요가 없어 비워둠
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //사용자가 입력한 데이터 값을 갖는 CharSequence 객체의 toString 을 호출, 이 문자열은 crime 의 제목을 구성하는데 사용
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //해당 부분은 구현 필요가 없어 비워둠
            }
        }
        titleField.addTextChangedListener(titleWatcher)     //editText 의 변화를 감지하여 crime.title 을 바꿔주는 역할

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->    //view, boolean 두 인자를 받아서 사용, _는 필요없어서 생략한다는 의미
                crime.isSolved = isChecked                  //checkBox 에서 체크되었는지 여부에 따라 crime.isSolved 를 변경함
            }
        }
    }
}