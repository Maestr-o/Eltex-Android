package com.eltex.androidschool.repository.di

import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.repository.NetworkEventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface EventRepositoryModule {
    @Binds
    fun bindEventRepository(impl: NetworkEventRepository): EventRepository
}