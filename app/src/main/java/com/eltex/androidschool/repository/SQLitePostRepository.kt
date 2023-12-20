package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.PostsDao
import com.eltex.androidschool.entity.PostEntity
import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SQLitePostRepository(private val dao: PostsDao) : PostRepository {
    override fun getPosts(): Flow<List<Post>> = dao.getAll()
        .map {
            it.map(PostEntity::toPost)
        }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun savePost(id: Long, content: String) {
        val idx = dao.getPostById(id)
        if (idx == null) {
            dao.save(
                PostEntity.fromPost(
                    Post(
                        id = id,
                        content = content,
                        author = "Me",
                        published = "13.12.2023 20:50",
                    )
                )
            )
        } else {
            dao.save(dao.getPostById(id).copy(content = content))
        }
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

}