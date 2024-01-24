package com.eltex.androidschool.model

import com.eltex.androidschool.utils.Either

sealed interface EventMessage {
    // => к серверу
    data object LoadNextPage : EventMessage
    data object Refresh : EventMessage
    data class Like(val event: EventUiModel) : EventMessage
    data class Participate(val event: EventUiModel) : EventMessage
    data class Delete(val event: EventUiModel) : EventMessage
    data object HandleError : EventMessage

    // <= от сервера
    data class DeleteError(val error: EventWithError) : EventMessage
    data class LikeResult(val result: Either<EventWithError, EventUiModel>) : EventMessage
    data class ParticipateResult(val result: Either<EventWithError, EventUiModel>) : EventMessage
    data class InitialLoaded(val result: Either<Throwable, List<EventUiModel>>) : EventMessage
    data class NextPageLoaded(val result: Either<Throwable, List<EventUiModel>>) : EventMessage
}