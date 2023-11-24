package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryPostRepository : PostRepository {
    private val state = MutableStateFlow(
        Post(
            id = 1L,
            content = "Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            likedByMe = false,
        )
    )

    override fun getPost(): Flow<Post> = state.asStateFlow()

    override fun like() {
        state.update {
            it.copy(likedByMe = !it.likedByMe)
        }
    }
}