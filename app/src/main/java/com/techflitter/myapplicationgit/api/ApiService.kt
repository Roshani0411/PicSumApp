package com.techflitter.myapplicationgit.api

import com.techflitter.myapplicationgit.model.PicSumModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/list")
    suspend fun getData(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PicSumModel>

}