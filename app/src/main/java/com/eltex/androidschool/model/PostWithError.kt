package com.eltex.androidschool.model

data class PostWithError(
    val postUiModel: PostUiModel,
    val throwable: Throwable,
)