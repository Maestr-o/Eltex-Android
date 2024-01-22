package com.eltex.androidschool.repository

import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Post

class NetworkPostRepository(
    private val api: PostsApi
) : PostRepository {

    override suspend fun getLatest(count: Int): List<Post> = api.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Post> = api.getBefore(id, count)

    override suspend fun likeById(id: Long): Post = api.like(id)

    override suspend fun unlikeById(id: Long): Post = api.unlike(id)

    override suspend fun savePost(id: Long, content: String): Post = api.save(
        Post(
            id = id,
            content = content,
        )
    )

    override suspend fun deleteById(id: Long) = api.delete(id)
}