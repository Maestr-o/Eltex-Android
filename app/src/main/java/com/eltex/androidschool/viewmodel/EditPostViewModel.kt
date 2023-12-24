package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditPostViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewPostUiState())
    val state = _state.asStateFlow()

    fun update(post: Post) {
        _state.update {
            it.copy(result = post)
        }
    }

    fun editById(content: String) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        val post = state.value.result
        if (post != null) {
            repository.savePost(
                id = post.id,
                content = content,
                object : Callback<Post> {
                    override fun onSuccess(data: Post) {
                        _state.update { ui ->
                            ui.copy(
                                result = data,
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
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}