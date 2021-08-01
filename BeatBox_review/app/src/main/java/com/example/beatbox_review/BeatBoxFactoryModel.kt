package com.example.beatbox_review

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BeatBoxFactoryModel(val assets: AssetManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AssetManager::class.java).newInstance(assets)
    }
}