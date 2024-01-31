package com.eltex.androidschool.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId

@InstallIn(SingletonComponent::class)
@Module
class DateTimeFormatterModule {

    @Provides
    fun provideZoneId(): ZoneId = ZoneId.systemDefault()
}