package com.example.beatbox_review

private const val WAV = ".wav"

class Sound(val assetPath: String, var soundId: Int? = null) {
    val name = assetPath.split("/").last().removeSuffix(".wav") // / 로 잘라내어 마지막 부분을 가져오는데, .wav 는 삭제하고 가져옴
}