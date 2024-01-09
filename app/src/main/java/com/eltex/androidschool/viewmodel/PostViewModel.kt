package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.SchedulersFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper = PostUiModelMapper(),
    private val schedulersFactory: SchedulersFactory = SchedulersFactory.DEFAULT,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _state = MutableStateFlow(PostUiState())
    val state: StateFlow<PostUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            it.copy(status = Status.Loading)
        }

        repository.getPosts()
            .observeOn(schedulersFactory.computation())
            .map { posts ->
                posts.map {
                    mapper.map(it)
                }
            }
            .observeOn(schedulersFactory.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    _state.update {
                        it.copy(posts = data, status = Status.Idle)
                    }
                },
                onError = { throwable ->
                    _state.update {
                        it.copy(status = Status.Error(throwable))
                    }
                }
            )
            .addTo(disposable)
    }

    fun likeById(post: PostUiModel) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        if (!post.likedByMe) {
            repository.likeById(post.id)
                .observeOn(schedulersFactory.computation())
                .map {
                    mapper.map(it)
                }
                .observeOn(schedulersFactory.mainThread())
                .subscribeBy(
                    onSuccess = { data ->
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
                    },
                    onError = { throwable ->
                        _state.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                )
                .addTo(disposable)
        } else {
            repository.unlikeById(post.id)
                .observeOn(schedulersFactory.computation())
                .map {
                    mapper.map(it)
                }
                .observeOn(schedulersFactory.mainThread())
                .subscribeBy(
                    onSuccess = { data ->
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
                    },
                    onError = { throwable ->
                        _state.update {
                            it.copy(status = Status.Error(throwable))
                        }
                    }
                )
                .addTo(disposable)
        }
    }

    fun deleteById(id: Long) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        repository.deleteById(id)
            .subscribeBy(
                onComplete = {
                    _state.update { state ->
                        state.copy(
                            posts = state.posts.orEmpty()
                                .filter {
                                    it.id != id
                                },
                            status = Status.Idle
                        )
                    }
                },
                onError = { throwable ->
                    _state.update {
                        it.copy(status = Status.Error(throwable))
                    }
                }
            )
            .addTo(disposable)
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

    override fun onCleared() {
        disposable.dispose()
    }
}