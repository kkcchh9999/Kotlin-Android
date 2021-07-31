package com.example.beatbox_review

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 5

class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)  //최대 동시 재생 갯수
        .build()

    init {
        sounds = loadSounds()
    }

    private fun loadSounds(): List<Sound> {     //sound 읽기 - asset 파일 읽기

        val soundNames: Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!   //asset 의 sample_sound
        } catch (e: Exception) {
            return emptyList()
        }

        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->    //sample_sound 의 각각의 요소들에게
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)    //Sound 에 파일 이름 할당
            try {
                load(sound)
                sounds.add(sound)
            } catch (ioe: IOException) {
                Log.e(TAG, "Could not load sound $filename", ioe)
            }
        }

        return sounds                       //sounds 리턴
    }

    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    fun play(sound: Sound) {
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, PLAY_SPEED)
        }
    }

    fun release() {
        soundPool.release()
    }
}