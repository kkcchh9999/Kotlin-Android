package com.example.photogallery_review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery_review.api.FlickrApi
import com.example.photogallery_review.api.FlickrResponse
import com.example.photogallery_review.api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetcher"

class FlickrFetcher {

    private val flickrApi: FlickrApi
    init {
        val retrofit: Retrofit = Retrofit.Builder() //retrofit builder 를 통해 retrofit 인스턴스 구성
            .baseUrl("https://api.flickr.com/") //url 지정
            .addConverterFactory(GsonConverterFactory.create())  //converter 를 통해 Call 의 객체 형식 변경 (string 으로)
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java) //retrofit 인스턴스에 api 정보 사용
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val flickrRequest: Call<FlickrResponse> = flickrApi.fetchPhotos()

        flickrRequest.enqueue(object : Callback<FlickrResponse> {   //enqueue 는 비동기로 실행됨. Retrofit 이 백그라운드 스레드를 관리
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {   //컨버터로 String 으로 변경했음으로 Response 가 String 반환
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }
        })

        return responseLiveData
    }
}