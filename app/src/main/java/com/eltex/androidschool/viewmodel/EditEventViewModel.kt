package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditEventViewModel(
    private val repository: EventRepository,
) : ViewModel() {

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
            viewModelScope.launch {
                try {
                    val data = repository.saveEvent(event.id, content)
                    _state.update { ui ->
                        ui.copy(result = data, status = Status.Idle)
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(status = Status.Error(e))
                    }
                }
            }
        }
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}