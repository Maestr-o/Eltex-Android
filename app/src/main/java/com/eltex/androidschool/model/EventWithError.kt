package com.eltex.androidschool.model

data class EventWithError(
    val eventUiModel: EventUiModel,
    val throwable: Throwable,
)