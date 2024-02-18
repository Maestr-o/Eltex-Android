package com.eltex.androidschool.api

import com.eltex.androidschool.model.Media
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MediaApi {
    @Multipart
    @POST("api/media")
    suspend fun upload(@Part file: MultipartBody.Part): Media
}