package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper = PostUiModelMapper(),
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

        viewModelScope.launch {
            try {
                val posts: List<PostUiModel> = repository.getPosts().map {
                    mapper.map(it)
                }
                _state.update {
                    it.copy(posts = posts, status = Status.Idle)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun likeById(post: PostUiModel) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        viewModelScope.launch {
            try {
                val uiModel: PostUiModel = if (!post.likedByMe) {
                    mapper.map(repository.likeById(post.id))
                } else {
                    mapper.map(repository.unlikeById(post.id))
                }
                _state.update { state ->
                    state.copy(
                        posts = state.posts.orEmpty()
                            .map {
                                if (it.id == post.id) {
                                    uiModel
                                } else {
                                    it
                                }
                            },
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun deleteById(id: Long) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        viewModelScope.launch {
            try {
                repository.deleteById(id)
                _state.update { state ->
                    state.copy(
                        posts = state.posts.orEmpty()
                            .filter {
                                it.id != id
                            },
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
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