package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditPostViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewPostUiState())
    val state = _state.asStateFlow()

    fun update(post: PostUiModel) {
        _state.update {
            it.copy(
                result = Post(
                    id = post.id,
                    content = post.content,
                    likedByMe = post.likedByMe,
                )
            )
        }
    }

    fun editById(content: String) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        val post = state.value.result
        if (post != null) {
            viewModelScope.launch {
                try {
                    val data = repository.savePost(post.id, content)
                    _state.update { it.copy(result = data, status = Status.Idle) }
                } catch (e: Exception) {
                    _state.update { it.copy(status = Status.Error(e)) }
                }
            }
        }
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}