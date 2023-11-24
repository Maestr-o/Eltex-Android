package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(): Flow<Post>
    fun like()
}