package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.Post

data class PostUiState(
    val posts: List<Post> = emptyList(),
)