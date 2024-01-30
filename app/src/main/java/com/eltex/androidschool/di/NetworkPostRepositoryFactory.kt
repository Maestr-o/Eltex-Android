package com.eltex.androidschool.di

import android.content.ContentResolver
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.repository.NetworkPostRepository
import com.eltex.androidschool.repository.PostRepository

class NetworkPostRepositoryFactory(
    private val postsApi: PostsApi,
    private val mediaApi: MediaApi,
    private val contentResolver: ContentResolver,
) : PostRepositoryFactory {
    override fun create(): PostRepository =
        NetworkPostRepository(
            postsApi, mediaApi, contentResolver
        )
}