package com.eltex.androidschool.api.di

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.api.PostsApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    companion object {
        private val JSON_TYPE = "application/json".toMediaType()
        private val JSON = Json {
            ignoreUnknownKeys = true
        }
    }

    @Singleton
    @Provides
    fun provideOkHttp() = OkHttpClient.Builder()
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

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://eltex-android.ru/")
        .client(okHttp)
        .addConverterFactory(JSON.asConverterFactory(JSON_TYPE))
        .build()

    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create()

    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create()
}