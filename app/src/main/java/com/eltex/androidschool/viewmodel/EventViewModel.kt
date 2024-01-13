package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper = EventUiModelMapper(),
) : ViewModel() {

    private val _state = MutableStateFlow(EventUiState())
    val state: StateFlow<EventUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            it.copy(status = Status.Loading)
        }

        viewModelScope.launch {
            try {
                val events = repository.getEvents().map {
                    mapper.map(it)
                }
                _state.update {
                    it.copy(events = events, status = Status.Idle)
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun likeById(event: EventUiModel) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        viewModelScope.launch {
            try {
                val uiModel: EventUiModel = if (!event.likedByMe) {
                    mapper.map(repository.likeById(event.id))
                } else {
                    mapper.map(repository.unlikeById(event.id))
                }
                _state.update { state ->
                    state.copy(
                        events = state.events.orEmpty()
                            .map {
                                if (it.id == event.id) {
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

    fun participateById(event: EventUiModel) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        viewModelScope.launch {
            try {
                val uiModel: EventUiModel = if (!event.participatedByMe) {
                    mapper.map(repository.participateById(event.id))
                } else {
                    mapper.map(repository.unparticipateById(event.id))
                }
                _state.update { state ->
                    state.copy(
                        events = state.events.orEmpty()
                            .map {
                                if (it.id == event.id) {
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
                        events = state.events.orEmpty()
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