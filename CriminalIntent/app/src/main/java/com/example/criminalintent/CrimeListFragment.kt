package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(){

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel : CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)  //리니어 레이아웃으로 설정, 해당부분 설정 안하면 작동 X

        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes  //뷰모델의 mutableList 가져오기
        adapter = CrimeAdapter(crimes)          //어댑터 생성
        crimeRecyclerView.adapter = adapter     //어댑터와 리사이클러뷰 연결
    }

    private inner class CrimeHolder1(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {    //onClickListener
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class CrimeHolder2(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val needPolice: TextView = itemView.findViewById(R.id.tv_Police)    //chap9 챌린지
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date
            needPolice.setOnClickListener {         //chap9 챌린지
                Toast.makeText(context, "Police!", Toast.LENGTH_SHORT).show()
            }
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {    //onClickListener
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {  //Chap9 챌린지
            return crimes[position].requiresPolice          //onCreateViewHolder 함수에 들어가는 viewType 을
        }                                                   //설정해준다.

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {

            val view: View?
            return when (viewType) {    //viewType 에 따라 다르게 할당
                1 -> {
                    view = LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_crime,
                        parent,
                        false
                    )
                    CrimeHolder1(view)
                }
                else -> {
                    view = LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_crime_police,
                        parent,
                        false
                    )
                    CrimeHolder2(view)
                }
            }
        }   //onCreate

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { //바인딩
            when(crimes[position].requiresPolice) {
                1 -> {
                    (holder as CrimeHolder1).bind(crimes[position])
                    holder.setIsRecyclable(false)
                }
                else -> {
                    (holder as CrimeHolder2).bind(crimes[position])
                    holder.setIsRecyclable(false)
                }
            }
        }
        override fun getItemCount() = crimes.size   //사이즈 반환
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}