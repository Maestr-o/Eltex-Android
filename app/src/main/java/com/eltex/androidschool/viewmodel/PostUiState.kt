package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status

data class PostUiState(
    val posts: List<PostUiModel>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean = status == Status.Loading && posts != null
    val isEmptyLoading: Boolean = status == Status.Loading && posts == null
    val emptyError: Throwable? = (status as? Status.Error)?.reason?.takeIf {
        posts == null
    }
    val refreshingError: Throwable? = (status as? Status.Error)?.reason?.takeIf {
        posts != null
    }
}