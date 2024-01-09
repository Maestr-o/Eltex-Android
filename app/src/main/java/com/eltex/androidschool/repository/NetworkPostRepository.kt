package com.eltex.androidschool.repository

import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Post
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class NetworkPostRepository(
    private val api: PostsApi
) : PostRepository {

    override fun getPosts(): Single<List<Post>> = api.getAll()

    override fun likeById(id: Long): Single<Post> = api.like(id)

    override fun unlikeById(id: Long): Single<Post> = api.unlike(id)

    override fun savePost(id: Long, content: String): Single<Post> = api.save(
        Post(
            id = id,
            content = content,
        )
    )

    override fun deleteById(id: Long): Completable = api.delete(id)
}