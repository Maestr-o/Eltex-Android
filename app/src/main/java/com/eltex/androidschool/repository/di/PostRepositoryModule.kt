package com.eltex.androidschool.repository.di

import com.eltex.androidschool.repository.NetworkPostRepository
import com.eltex.androidschool.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface PostRepositoryModule {
    @Binds
    fun bindPostRepository(impl: NetworkPostRepository): PostRepository
}