package com.example.criminalintent_review

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())
    //빈 리스트를 가지고 있다가 라이브 데이터가 반환되면 내용을 교체

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
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
            Toast.makeText(
                context,
                "${crime.title} pressed!",
                Toast.LENGTH_SHORT
            ).show()
        }
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
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }
}