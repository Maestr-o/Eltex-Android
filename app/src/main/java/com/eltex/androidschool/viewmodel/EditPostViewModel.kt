package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditPostViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()
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
            repository.savePost(post.id, content = content)
                .subscribeBy(
                    onSuccess = { data ->
                        _state.update { ui ->
                            ui.copy(
                                result = data,
                                status = Status.Idle,
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

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }

    override fun onCleared() {
        disposable.dispose()
    }
}