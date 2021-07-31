
package com.example.beatbox_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beatbox_review.databinding.ActivityMainBinding
import com.example.beatbox_review.databinding.ListItemSoundBinding

var PLAY_SPEED = 1.0f

class MainActivity : AppCompatActivity() {

    private lateinit var beatBox: BeatBox
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beatBox = BeatBox(assets)
        seekBar = findViewById(R.id.seekbar)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)    //데이터 바인딩 클래스

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)   //리사이클러 뷰의 레이아웃 매니저
            adapter = SoundAdapter(beatBox.sounds)
        }

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                PLAY_SPEED = p1.toFloat()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                PLAY_SPEED = p0!!.progress.toFloat()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                PLAY_SPEED = p0!!.progress.toFloat()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
            RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = SoundViewModel(beatBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()    //즉시 동기화?
            }
        }

    }

    private inner class SoundAdapter(private val sounds: List<Sound>) :
            RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount() = sounds.size
    }
}