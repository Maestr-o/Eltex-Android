package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post

interface PostRepository {
    suspend fun getLatest(count: Int): List<Post>
    suspend fun getBefore(id: Long, count: Int): List<Post>
    suspend fun likeById(id: Long): Post
    suspend fun unlikeById(id: Long): Post
    suspend fun savePost(id: Long, content: String): Post
    suspend fun deleteById(id: Long)
}