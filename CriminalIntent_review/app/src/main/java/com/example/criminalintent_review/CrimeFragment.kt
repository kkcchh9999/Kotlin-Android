package com.example.criminalintent_review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //fragment 에서는 view 를 inflate 하지 않는다 = setContentView(R.layout.activity_main) 선언 X
        //view 는 onCreateView 에서 구성
        crime = Crime()
        val crimeID: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID    //UUID 가져오기
        crimeDetailViewModel.loadCrime(crimeID)
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { crime ->
                crime?.let{
                    this.crime = crime
                    updateUI()
                }
            }
        )
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

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply{
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.parentFragmentManager, DIALOG_DATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onDateSelected(date: Date) {   //DatePickerDialog 의 Callbacks 구현
        crime.date = date
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = DateFormat.format("EEE, MMM, dd, yyyy hh:mm", this.crime.date)
        solvedCheckBox.isChecked = crime.isSolved
    }

    companion object {

        fun newInstance(crimeID: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeID)
            }
            return CrimeFragment().apply {
                arguments = args    //Fragment 의 속성인 argument 는 setter 를 자동으로 호출, 따라서 setArguments(args) 와 같다.
            }
        }
    }
}