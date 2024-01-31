package com.eltex.androidschool.viewmodel

import dagger.assisted.AssistedFactory

@AssistedFactory
interface NewPostViewModelFactory {
    fun create(postId: Long): NewPostViewModel
}