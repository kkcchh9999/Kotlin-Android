package com.example.photogallery_review

import android.app.Application
import androidx.lifecycle.*

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrFetcher = FlickrFetcher()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String
    get() = mutableSearchTerm.value ?: ""

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)
        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {
                flickrFetcher.fetchPhotos()
            } else {
                flickrFetcher.searchPhotos(searchTerm)
            }
        }//#1 ViewModel 에서 fetchPhotos 를 호출하여 JSON 데이터 내려받음
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}