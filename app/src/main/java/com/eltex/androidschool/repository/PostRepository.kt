package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PostRepository {
    fun getPosts(): Single<List<Post>>
    fun likeById(id: Long): Single<Post>
    fun unlikeById(id: Long): Single<Post>
    fun savePost(id: Long, content: String): Single<Post>
    fun deleteById(id: Long): Completable
}