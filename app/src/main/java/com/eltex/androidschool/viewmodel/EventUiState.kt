package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status

data class EventUiState(
    val events: List<Event>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean = status == Status.Loading && events != null
    val isEmptyLoading: Boolean = status == Status.Loading && events == null
    val emptyError: Throwable? = (status as? Status.Error)?.reason?.takeIf {
        events == null
    }
    val refreshingError: Throwable? = (status as? Status.Error)?.reason?.takeIf {
        events != null
    }
}