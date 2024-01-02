package com.eltex.androidschool.repository

import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.utils.Callback
import retrofit2.Call
import retrofit2.Response

class NetworkPostRepository(
    private val api: PostsApi
) : PostRepository {

    override fun getPosts(callback: Callback<List<Post>>) {
        api.getAll().enqueue(
            object : retrofit2.Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun likeById(id: Long, callback: Callback<Post>) {
        api.like(id).enqueue(
            object : retrofit2.Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun unlikeById(id: Long, callback: Callback<Post>) {
        api.unlike(id).enqueue(
            object : retrofit2.Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun savePost(id: Long, content: String, callback: Callback<Post>) {
        api.save(
            Post(
                id = id,
                content = content,
            )
        ).enqueue(
            object : retrofit2.Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: Callback<Unit>) {
        api.delete(id).enqueue(
            object : retrofit2.Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }
}