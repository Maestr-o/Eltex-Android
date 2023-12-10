package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.PostsDao
import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SQLitePostRepository(
    private val dao: PostsDao
) : PostRepository {

    private val state = MutableStateFlow(readPosts())

    private fun readPosts(): List<Post> = dao.getAll()

    override fun getPosts(): Flow<List<Post>> = state.asStateFlow()

    override fun likeById(id: Long) {
        dao.likeById(id)
        state.update { readPosts() }
    }

    override fun addPost(content: String) {
        dao.save(
            Post(
                content = content,
                author = "Me",
                published = "09.12.2023 21:14",
            )
        )
        state.update { readPosts() }
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        state.update { readPosts() }
    }

    override fun editById(id: Long, text: String) {
        dao.save(
            dao.getPostById(id).copy(
                id = id,
                content = text,
            )
        )
        state.update { readPosts() }
    }
}