package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.PostDao
import com.eltex.androidschool.entity.PostEntity
import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SQLitePostRepository(private val dao: PostDao) : PostRepository {
    override fun getPosts(): Flow<List<Post>> = dao.getAll()
        .map {
            it.map(PostEntity::toPost)
        }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun addPost(content: String) {
        dao.save(
            PostEntity.fromPost(
                Post(
                    content = content,
                    author = "Me",
                    published = "13.12.2023 20:50",
                )
            )
        )
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override fun editById(id: Long, text: String) {
        dao.save(
            PostEntity.fromPost(
                dao.getPostById(id).toPost().copy(
                    id = id,
                    content = text,
                )
            )
        )
    }
}