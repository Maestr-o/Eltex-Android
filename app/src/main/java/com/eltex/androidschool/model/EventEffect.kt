package com.eltex.androidschool.model

sealed interface EventEffect {
    data class LoadNextPage(val id: Long, val count: Int) : EventEffect
    data class LoadInitialPage(val count: Int) : EventEffect
    data class Like(val event: EventUiModel) : EventEffect
    data class Participate(val event: EventUiModel) : EventEffect
    data class Delete(val event: EventUiModel) : EventEffect
}