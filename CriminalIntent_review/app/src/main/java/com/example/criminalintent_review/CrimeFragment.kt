package com.example.criminalintent_review

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATE = 0
private const val REQUEST_TIME = 1
private const val DATE_FORMAT = "yyyy년 M월 d일 H시 m분, E요일"
private const val REQUEST_CONTACT = 2

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var reportButton: Button
    private lateinit var suspectButton: Button
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
        timeButton = view.findViewById(R.id.crime_time) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        reportButton = view.findViewById(R.id.crime_report) as Button
        suspectButton = view.findViewById(R.id.crime_suspect) as Button

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

        timeButton.setOnClickListener {
            TimePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_TIME)
                show(this@CrimeFragment.parentFragmentManager, DIALOG_TIME)
            }
        }

        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {  //SEND 에 해당하는 앱들을 보여줌
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.crime_report_subject)
                )
            }.also { intent ->
                val chooserIntent =
                    Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }

        suspectButton.apply {   //연락처 선택 인텐트
            val pickContactIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }

            val packageManager: PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(pickContactIntent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onDateSelected(date: Date) {   //DatePickerDialog 의 Callbacks 구현
        crime.date = date   //범죄 데이터의 시간을 설정된 시간으로 설정
        updateUI()          //UI 업데이트
    }

    override fun onTimeSelected(date: Date) {   //TimePickerDialog 의 Callbacks 구현
        crime.date = date
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return

            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri = data.data ?: return
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)   //연락처의 모든 표시명 가져오기
                val cursor = requireActivity().contentResolver  //이를 읽을 커서 객체
                    .query(contactUri, queryFields, null, null, null)
                cursor?.use {
                    if (it.count == 0) {
                        return
                    }
                    it.moveToFirst()    //커서를 제일 처음으로 옮김
                    val suspect = it.getString(0)   //getString(0) 첫 번째 열 값 = 이름
                    crime.suspect = suspect
                    crimeDetailViewModel.saveCrime(crime)   // 데이터베이스에 저장
                    suspectButton.text = suspect
                }
            }
        }
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = DateFormat.format("EEE, MMM, dd, yyyy", this.crime.date)
        timeButton.text = DateFormat.format("hh:mm", this.crime.date)
        solvedCheckBox.isChecked = crime.isSolved

        if(crime.suspect.isNotEmpty()) {    //버튼 텍스트 설정
            suspectButton.text = crime.suspect
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspect = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(R.string.crime_report,crime.title, dateString, solvedString, suspect)
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