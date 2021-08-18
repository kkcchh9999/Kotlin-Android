package com.example.photogallery_review.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=abb2dfac21b6b47664fdf14f16e0577d" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET    //GET 뒤에 () 없이 @Url 과 함께 사용하면 알아서 @Url 에 해당하는 Url 사용
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody> //내려받을 위치 URL 인자로 받음
}