package com.eltex.androidschool.dao

import com.eltex.androidschool.model.Post

interface PostsDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun deleteById(id: Long)
    fun likeById(id: Long): Post
}