package com.example.photogallery_review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel : ViewModel() {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        galleryItemLiveData = FlickrFetcher().fetchPhotos() //#1 ViewModel 에서 fetchPhotos 를 호출하여 JSON 데이터 내려받음
    }
}