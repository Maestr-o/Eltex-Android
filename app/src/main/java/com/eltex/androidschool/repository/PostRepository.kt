package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    fun likeById(id: Long)
    fun addPost(content: String)
    fun deleteById(id: Long)
}