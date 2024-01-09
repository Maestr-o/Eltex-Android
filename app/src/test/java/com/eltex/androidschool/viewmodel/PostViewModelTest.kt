package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.TestSchedulersFactory
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PostViewModelTest {

    @Test
    fun `deleteById error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = PostViewModel(
            repository = object : PostRepository {
                override fun getPosts(): Single<List<Post>> = Single.just(emptyList())

                override fun likeById(id: Long): Single<Post> = Single.never()

                override fun unlikeById(id: Long): Single<Post> = Single.never()

                override fun savePost(id: Long, content: String): Single<Post> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.error(testError)
            },
            schedulersFactory = TestSchedulersFactory()
        )
        viewModel.deleteById(111)
        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }
}