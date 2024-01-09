package com.eltex.androidschool.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

object RetrofitFactory {
    private val JSON_TYPE = "application/json".toMediaType()
    private val JSON = Json {
        ignoreUnknownKeys = true
    }

    val INSTANCE: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://eltex-android.ru/")
            .client(OkHttpFactory.INSTANCE)
            .addConverterFactory(JSON.asConverterFactory(JSON_TYPE))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}