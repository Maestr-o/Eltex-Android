package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewPostViewModel(
    private val repository: PostRepository,
    private val postId: Long = 0,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _state = MutableStateFlow(NewPostUiState())
    val state = _state.asStateFlow()

    fun save(content: String) {
        repository.savePost(postId, content)
            .subscribeBy(
                onSuccess = { data ->
                    _state.update { it.copy(result = data, status = Status.Idle) }
                },
                onError = { throwable ->
                    _state.update { it.copy(status = Status.Error(throwable)) }
                }
            )
            .addTo(disposable)
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }

    override fun onCleared() {
        disposable.dispose()
    }
}