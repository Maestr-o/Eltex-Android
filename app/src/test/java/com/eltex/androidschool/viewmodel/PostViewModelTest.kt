package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.MainDispatcherRule
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class PostViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `deleteById error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override suspend fun getPosts(): List<Post> = emptyList()

                override suspend fun likeById(id: Long): Post = error("Not implemented")

                override suspend fun unlikeById(id: Long): Post = error("Not implemented")

                override suspend fun savePost(id: Long, content: String): Post =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = throw testError
            },
        )
        viewModel.deleteById(111)
        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }
}