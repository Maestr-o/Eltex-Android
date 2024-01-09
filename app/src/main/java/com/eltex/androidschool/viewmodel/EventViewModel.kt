package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.SchedulersFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper = EventUiModelMapper(),
    private val schedulersFactory: SchedulersFactory = SchedulersFactory.DEFAULT,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _state = MutableStateFlow(EventUiState())
    val state: StateFlow<EventUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            it.copy(status = Status.Loading)
        }

        repository.getEvents()
            .observeOn(schedulersFactory.computation())
            .map { events ->
                events.map {
                    mapper.map(it)
                }
            }
            .observeOn(schedulersFactory.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    _state.update {
                        it.copy(events = data, status = Status.Idle)
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

    fun likeById(event: EventUiModel) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        if (!event.likedByMe) {
            repository.likeById(event.id)
                .observeOn(schedulersFactory.computation())
                .map {
                    mapper.map(it)
                }
                .observeOn(schedulersFactory.mainThread())
                .subscribeBy(
                    onSuccess = { data ->
                        _state.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    },
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
        } else {
            repository.unlikeById(event.id)
                .observeOn(schedulersFactory.computation())
                .map {
                    mapper.map(it)
                }
                .observeOn(schedulersFactory.mainThread())
                .subscribeBy(
                    onSuccess = { data ->
                        _state.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    },
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

    fun participateById(event: EventUiModel) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        if (!event.participatedByMe) {
            repository.participateById(event.id)
                .observeOn(schedulersFactory.computation())
                .map {
                    mapper.map(it)
                }
                .observeOn(schedulersFactory.mainThread())
                .subscribeBy(
                    onSuccess = { data ->
                        _state.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    },
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
        } else {
            repository.unparticipateById(event.id)
                .observeOn(schedulersFactory.computation())
                .map {
                    mapper.map(it)
                }
                .observeOn(schedulersFactory.mainThread())
                .subscribeBy(
                    onSuccess = { data ->
                        _state.update { state ->
                            state.copy(
                                events = state.events.orEmpty()
                                    .map {
                                        if (it.id == event.id) {
                                            data
                                        } else {
                                            it
                                        }
                                    },
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

    fun deleteById(id: Long) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        repository.deleteById(id)
            .subscribeBy(
                onComplete = {
                    _state.update { state ->
                        state.copy(
                            events = state.events.orEmpty()
                                .filter {
                                    it.id != id
                                },
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

    fun consumeError() {
        _state.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }
        }
    }

    override fun onCleared() {
        disposable.dispose()
    }
}