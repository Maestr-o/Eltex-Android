package com.eltex.androidschool.repository

import android.content.ContentResolver
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Media
import com.eltex.androidschool.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class NetworkPostRepository @Inject constructor(
    private val postsApi: PostsApi,
    private val mediaApi: MediaApi,
    private val contentResolver: ContentResolver,
) : PostRepository {

    override suspend fun getLatest(count: Int): List<Post> = postsApi.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Post> = postsApi.getBefore(id, count)

    override suspend fun likeById(id: Long): Post = postsApi.like(id)

    override suspend fun unlikeById(id: Long): Post = postsApi.unlike(id)

    override suspend fun savePost(id: Long, content: String, fileModel: FileModel?): Post {
        val post = when {
            fileModel?.uri?.scheme == "content" -> {
                val media = upload(fileModel)
                Post(
                    id = id,
                    content = content,
                    attachment = Attachment(media.url, fileModel.type)
                )
            }

            fileModel != null -> {
                Post(
                    id = id,
                    content = content,
                    attachment = Attachment(fileModel.uri.toString(), fileModel.type)
                )
            }

            else -> Post(
                id = id,
                content = content,
            )
        }

        return postsApi.save(post)
    }

    override suspend fun deleteById(id: Long) = postsApi.delete(id)

    private suspend fun upload(fileModel: FileModel): Media =
        mediaApi.upload(
            MultipartBody.Part.createFormData(
                "file",
                "file",
                withContext(Dispatchers.IO) {
                    requireNotNull(contentResolver.openInputStream(fileModel.uri)).use {
                        it.readBytes()
                    }
                        .toRequestBody()
                }
            )
        )
}