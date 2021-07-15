package com.example.criminalintent_review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)      //리사이클러뷰의 레이아웃 매니저, 해당 부분을 다른 레이아웃 매니저를 사용하면 모양을 바꿀 수 있음음

        updateUI()  //RecyclerView adapter 에 viewModel 의 list 를 입력하여 호출

        return view
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
            dateTextView.text = this.crime.date.toString()
            solvedImage.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.INVISIBLE
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
            return if (viewType == 1) {         //chap9 챌린지, 리사이클러뷰의 뷰를 다르게 사용하기
                val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                CrimeHolder(view)               //viewHolder 를 생성하면 유사한게 아닌 전혀 다른 형태 또한 사용 가능함.
            } else {
                val view = layoutInflater.inflate(R.layout.list_item_crime_police, parent, false)
                CrimeHolder(view)
            }

        }

        override fun getItemCount() = crimes.size

        override fun getItemViewType(position: Int): Int {  //해당 함수로 viewType 를 설정함
            return when(crimes[position].requirePolice) {
                true -> 1
                false -> 2
            }
        }

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

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }
}