package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditEventViewModel(
    private val repository: EventRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    fun update(event: EventUiModel) {
        _state.update {
            it.copy(
                result = Event(
                    id = event.id,
                    content = event.content,
                    likedByMe = event.likedByMe,
                    participatedByMe = event.participatedByMe,
                )
            )
        }
    }

    fun editById(content: String) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        val event = state.value.result
        if (event != null) {
            repository.saveEvent(event.id, content)
                .subscribeBy(
                    onSuccess = { data ->
                        _state.update { ui ->
                            ui.copy(
                                result = data,
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

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}