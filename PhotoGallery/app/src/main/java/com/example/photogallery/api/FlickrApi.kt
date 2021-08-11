package com.example.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
        "&api_key=abb2dfac21b6b47664fdf14f16e0577d" +
        "&format=json" +
        "&nojsoncallback=1" +
        "&extras=url_s"
    )
    fun fetchPhotos(): Call<FlickrResponse>
}