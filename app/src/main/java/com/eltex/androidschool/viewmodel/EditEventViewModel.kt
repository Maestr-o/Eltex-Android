package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditEventViewModel(
    private val repository: EventRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    fun update(event: Event) {
        _state.update {
            it.copy(result = event)
        }
    }

    fun editById(content: String) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        val event = state.value.result
        if (event != null) {
            repository.saveEvent(
                id = event.id,
                content = content,
                object : Callback<Event> {
                    override fun onSuccess(data: Event) {
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