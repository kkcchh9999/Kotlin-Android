package com.example.beatbox_review

import android.content.res.AssetManager
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"

class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>

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
            sounds.add(sound)               //리턴값 sounds 리스트에 추가
        }

        return sounds                       //sounds 리턴
    }
}