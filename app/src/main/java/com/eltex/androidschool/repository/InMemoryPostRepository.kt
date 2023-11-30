package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryPostRepository : PostRepository {
    private val state = MutableStateFlow(
        List(20) {
            Post(
                id = (it + 1).toLong(),
                content = "$it. Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                likedByMe = false,
            )
        }
            .reversed()
    )

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
    }
}