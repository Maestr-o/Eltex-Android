package com.eltex.androidschool.api

import com.eltex.androidschool.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpFactory {
    val INSTANCE by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .addHeader("Api-Key", BuildConfig.API_KEY)
                        .addHeader("Authorization", BuildConfig.AUTH_TOKEN)
                        .build()
                )
            }
            .let {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                } else {
                    it
                }
            }
            .build()
    }
}