package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun likeById(id: Long): Post
    suspend fun unlikeById(id: Long): Post
    suspend fun savePost(id: Long, content: String): Post
    suspend fun deleteById(id: Long)
}