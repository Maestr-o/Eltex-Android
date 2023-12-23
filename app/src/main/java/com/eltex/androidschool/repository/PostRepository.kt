package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.utils.Callback

interface PostRepository {
    fun getPosts(callback: Callback<List<Post>>)
    fun likeById(id: Long, callback: Callback<Post>)
    fun savePost(id: Long, content: String, callback: Callback<Post>)
    fun deleteById(id: Long, callback: Callback<Unit>)

}