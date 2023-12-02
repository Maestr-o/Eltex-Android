package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        repository.getPosts()
            .onEach { posts ->
                _uiState.update {
                    it.copy(posts = posts)
                }
            }
            .launchIn(viewModelScope)
    }

    fun likeById(id: Long) {
        repository.likeById(id)
    }

    fun addPost(content: String) {
        repository.addPost(content)
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}