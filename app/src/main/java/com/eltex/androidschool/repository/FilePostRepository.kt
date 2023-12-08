package com.eltex.androidschool.repository

import android.content.Context
import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FilePostRepository(
    private val context: Context
) : PostRepository {

    private companion object {
        const val POST_FILENAME = "posts.json"
        const val ID_FILENAME = "id.txt"
    }

    private val state = MutableStateFlow(readPosts())
    private var nextId = readId()

    private fun readPosts(): List<Post> {
        val file = context.filesDir.resolve(POST_FILENAME)
        val serializedPosts = if (file.exists()) {
            file.bufferedReader()
                .use {
                    it.readLine()
                }
        } else {
            null
        }

        return if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else {
            emptyList()
        }
    }

    private fun readId(): Long {
        val file = context.filesDir.resolve(ID_FILENAME)
        return if (file.exists()) {
            file.bufferedReader()
                .use {
                    it.readLine().toLong() ?: 0L
                }
        } else {
            0L
        }
    }

    override fun getPosts(): Flow<List<Post>> = state.asStateFlow()

    override fun likeById(id: Long) {
        state.update {
            it.map { post ->
                if (post.id == id) {
                    post.copy(likedByMe = !post.likedByMe)
                } else
                    post
            }
        }
        sync()
    }

    override fun addPost(content: String) {
        state.update { posts ->
            buildList(posts.size + 1) {
                add(
                    Post(
                        id = ++nextId,
                        content = content,
                        author = "Me",
                        published = "02.12.2023 21:05"
                    )
                )
                addAll(posts)
            }
        }
        sync()
    }

    override fun deleteById(id: Long) {
        state.update { posts ->
            posts.filter {
                it.id != id
            }
        }
        sync()
    }

    override fun editById(id: Long, text: String) {
        state.update { posts ->
            posts.map { post ->
                if (post.id == id) {
                    post.copy(content = text)
                } else
                    post
            }
        }
        sync()
    }

    private fun sync() {
        context.filesDir.resolve(POST_FILENAME)
            .bufferedWriter()
            .use {
                it.write(Json.encodeToString(state.value))
            }
        context.filesDir.resolve(ID_FILENAME)
            .bufferedWriter()
            .use {
                it.write(nextId.toString())
            }
    }
}