package com.example.criminalintent_review

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CrimeListFragment : Fragment() {
    /**
     * 호스팅 액티비티에서 구현할 인터페이스
     * 해당 프래그먼트를 사용하기위해서는 항상 구현해 주어야 함.
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks: Callbacks? = null    //callbacks


    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())    //빈 리스트를 가지고 있다가 라이브 데이터가 반환되면 내용을 교체
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }
    private lateinit var noCrime: TextView
    private lateinit var noCrimeAdd: ImageButton

    override fun onAttach(context: Context) {   //fragment 가 호스팅 activity 에 연결될 때 호출됨 <-> onDetach
        super.onAttach(context)                 //context 가 activity 보다 큰 개념임으로 context 를 사용하는것이 유연함
        callbacks = context as Callbacks?       //callbacks 를 호스팅 activity 에서 구현한 callbacks 로 설정
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //메뉴가 존재함을 알려줌
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)      //리사이클러뷰의 레이아웃 매니저, 해당 부분을 다른 레이아웃 매니저를 사용하면 모양을 바꿀 수 있음음
        crimeRecyclerView.adapter = adapter

        noCrime = view.findViewById(R.id.no_crime) as TextView
        noCrimeAdd = view.findViewById(R.id.no_crime_add) as ImageButton

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(   //옵저버 등록을 위해 사용하는 함수
            viewLifecycleOwner, //이 인자를 통해 뷰의 생명주기를 알 수 있고,
            Observer { crimes ->    //옵저버는 뷰가 유효할 때만 작동
                crimes?.let {
                    Log.i(TAG, "GotCrimes ${crimes.size}")
                    updateUI(crimes)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {  //메뉴가 있을 때 호출해서 사용
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)  //리소스 아이디로
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        val solvedImage: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            //chap10 챌린지 DateFormat 사용하기, 날짜 형식 편집 -> DateFormat.format("원하는 형식", date)
            dateTextView.text = DateFormat.format("EEE, MMM, dd, yyyy hh:mm", this.crime.date)
            solvedImage.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {
            callbacks?.onCrimeSelected(crime.id)    //callbacks 가 설정되었다면 onCrimeSelected 실행, UUID 인자로
        }                                           //이 함수는 호스트 activity 에서 구현.
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        //리사이클러뷰는 onCreateViewHolder 로 뷰홀더를 생성하고 난 이후에는 onBind 만 호출해서 내용을 바꿔가며 보여줌 -> Recycle 재활용

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)               //viewHolder 를 생성하면 유사한게 아닌 전혀 다른 형태 또한 사용 가능함.
        }

        override fun getItemCount() = crimes.size

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private fun updateUI(crimes: List<Crime>) { //라이브 데이터 관찰로 변경
        if (crimes.isEmpty()) {
            noCrime.visibility = View.VISIBLE
            noCrimeAdd.visibility = View.VISIBLE

            noCrimeAdd.setOnClickListener {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
            }
        }
        else {
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        }
    }
}