package com.example.pixbayphotos.network

import com.example.pixbayphotos.models.ImagesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("?imageType=photo")
    suspend fun fetchImages(
        @Query("key") key: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @Query("p") searchTerm: String
    ): Response<ImagesResponse>


}