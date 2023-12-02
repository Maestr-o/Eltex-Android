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

    private var nextId = state.value.first().id

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

    override fun addPost(content: String) {
        state.update { posts ->
            buildList(posts.size + 1) {
                add(
                    Post(
                        id = ++nextId,
                        content = content,
                        author = "Me",
                        published = "02.12.23 21:05"
                    )
                )
                addAll(posts)
            }
        }
    }

    override fun deleteById(id: Long) {
        state.update { posts ->
            posts.filter {
                it.id != id
            }
        }
    }
}