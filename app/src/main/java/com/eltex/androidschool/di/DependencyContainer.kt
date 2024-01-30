package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider

interface DependencyContainer {
    fun getPostViewModelFactory(): ViewModelProvider.Factory
    fun getNewPostViewModelFactory(): ViewModelProvider.Factory
    fun getEditPostViewModelFactory(): ViewModelProvider.Factory
    fun getEventViewModelFactory(): ViewModelProvider.Factory
    fun getNewEventViewModelFactory(): ViewModelProvider.Factory
    fun getEditEventViewModelFactory(): ViewModelProvider.Factory
}