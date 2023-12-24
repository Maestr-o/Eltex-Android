package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PostUiState())
    val state: StateFlow<PostUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            it.copy(status = Status.Loading)
        }

        repository.getPosts(
            object : Callback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _state.update {
                        it.copy(posts = data, status = Status.Idle)
                    }
                }

                override fun onError(throwable: Throwable) {
                    _state.update {
                        it.copy(status = Status.Error(throwable))
                    }
                }
            }
        )
    }

    fun likeById(post: Post) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        if (!post.likedByMe) {
            repository.likeById(
                post.id,
                object : Callback<Post> {
                    override fun onSuccess(data: Post) {
                        _state.update { state ->
                            state.copy(
                                posts = state.posts.orEmpty()
                                    .map {
                                        if (it.id == post.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    },
                                status = Status.Idle
                            )
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        _state.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                }
            )
        } else {
            // TODO
        }
    }

    fun deleteById(id: Long) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        repository.deleteById(
            id,
            object : Callback<Unit> {
                override fun onSuccess(data: Unit) {
                    _state.update { state ->
                        state.copy(
                            posts = state.posts.orEmpty()
                                .filter {
                                    it.id != id
                                },
                            status = Status.Idle
                        )
                    }
                }

                override fun onError(throwable: Throwable) {
                    _state.update {
                        it.copy(status = Status.Error(throwable))
                    }
                }
            }
        )
    }

    fun consumeError() {
        _state.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }
        }
    }

}