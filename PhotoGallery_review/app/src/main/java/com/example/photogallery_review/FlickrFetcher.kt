package com.example.photogallery_review

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery_review.api.FlickrApi
import com.example.photogallery_review.api.FlickrResponse
import com.example.photogallery_review.api.PhotoInterceptor
import com.example.photogallery_review.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder() //retrofit builder 를 통해 retrofit 인스턴스 구성
            .baseUrl("https://api.flickr.com/") //url 지정
            .addConverterFactory(GsonConverterFactory.create())  //converter 를 통해 Call 의 객체 형식 변경 (string 으로)
            .client(client)
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java) //retrofit 인스턴스에 api 정보 사용
    }

    fun fetchPhotoRequest(): Call<FlickrResponse> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(fetchPhotoRequest())
    }

    fun searchPhotosRequest(query: String): Call<FlickrResponse> {
        return flickrApi.searchPhotos(query)
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(searchPhotosRequest(query))
    }

    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()


        flickrRequest.enqueue(object : Callback<FlickrResponse> {   //enqueue 는 비동기로 실행됨. Retrofit 이 백그라운드 스레드를 관리
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {   //컨버터로 String 으로 변경했음으로 Response 가 String 반환
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems       //#2 내려받기가 끝나면  List 형태로 LiveData 객체로 전달,
                    ?: mutableListOf()                                                  //이로써 썸네일 크기의 사진이 있는 URL 을 각 GalleryItem 이 갖게됨
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }
        })

        return responseLiveData
    }

    @WorkerThread   //해당 에노테이션은 백그라운드 스레드에서 호출되어야 함을 나타냄, 다만 백그라운드에서 실행은 직접 해야함
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()   //call.execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)    //바이트 스트림을 decodeStream 을 통해 비트맵으로 변환
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")

        return bitmap
    }
}