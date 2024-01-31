package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.EventEffect
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.model.NoteStatus
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.utils.Either
import javax.inject.Inject

class EventReducer @Inject constructor() : Reducer<EventUiState, EventEffect, EventMessage> {

    companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
    }

    override fun reduce(
        old: EventUiState,
        message: EventMessage,
    ): ReducerResult<EventUiState, EventEffect> = when (message) {
        is EventMessage.Like -> ReducerResult(
            old.copy(
                events = old.events.map {
                    if (it.id == message.event.id) {
                        it.copy(
                            likedByMe = !it.likedByMe,
                            likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                        )
                    } else {
                        it
                    }
                }
            ),
            EventEffect.Like(message.event)
        )

        is EventMessage.Participate -> ReducerResult(
            old.copy(
                events = old.events.map {
                    if (it.id == message.event.id) {
                        it.copy(
                            participatedByMe = !it.participatedByMe,
                            participants = if (it.participatedByMe) {
                                it.participants - 1
                            } else {
                                it.participants + 1
                            }
                        )
                    } else {
                        it
                    }
                }
            ),
            EventEffect.Participate(message.event)
        )

        EventMessage.Retry -> ReducerResult(
            old.copy(status = NoteStatus.NextPageLoading),
            EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE),
        )

        EventMessage.LoadNextPage -> if (old.status == NoteStatus.Idle()) {
            ReducerResult(
                old.copy(status = NoteStatus.NextPageLoading),
                EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE),
            )
        } else {
            ReducerResult(old)
        }

        EventMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.events.isEmpty()) {
                    NoteStatus.EmptyLoading
                } else {
                    NoteStatus.Refreshing
                }
            ),
            EventEffect.LoadInitialPage(count = INITIAL_LOAD_SIZE),
        )

        is EventMessage.Delete -> ReducerResult(
            old.copy(events = old.events.filter { it.id != message.event.id }),
            EventEffect.Delete(message.event),
        )

        is EventMessage.DeleteError -> with(message.error) {
            ReducerResult(
                old.copy(
                    events = buildList(old.events.size + 1) {
                        addAll(old.events.takeWhile { it.id > eventUiModel.id })
                        add(eventUiModel)
                        addAll(old.events.takeLastWhile { it.id < eventUiModel.id })
                    },
                    singleError = throwable,
                )
            )
        }

        is EventMessage.NextPageLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    old.copy(status = NoteStatus.NextPageError(result.value))
                }

                is Either.Right -> old.copy(
                    events = old.events + result.value,
                    status = NoteStatus.Idle(result.value.size < PAGE_SIZE),
                )
            }
        )

        is EventMessage.LikeResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    val value = result.value
                    val event = value.eventUiModel
                    old.copy(
                        events = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        },
                        singleError = value.throwable,
                    )
                }

                is Either.Right -> {
                    val event = result.value
                    old.copy(
                        events = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        },
                    )
                }
            }
        )

        is EventMessage.ParticipateResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    val value = result.value
                    val event = value.eventUiModel
                    old.copy(
                        events = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        },
                        singleError = value.throwable,
                    )
                }

                is Either.Right -> {
                    val event = result.value
                    old.copy(
                        events = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        },
                    )
                }
            }
        )

        EventMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is EventMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    if (old.events.isEmpty()) {
                        old.copy(status = NoteStatus.EmptyError(result.value))
                    } else {
                        old.copy(singleError = result.value)
                    }
                }

                is Either.Right -> old.copy(
                    events = result.value,
                    status = NoteStatus.Idle(result.value.size < INITIAL_LOAD_SIZE),
                )
            }
        )
    }
}