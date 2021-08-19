package com.example.photogallery_review.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET    //GET 뒤에 () 없이 @Url 과 함께 사용하면 알아서 @Url 에 해당하는 Url 사용
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody> //내려받을 위치 URL 인자로 받음

    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>
}