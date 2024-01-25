package com.eltex.androidschool.model

data class PostUiState(
    val posts: List<PostUiModel> = emptyList(),
    val status: NoteStatus = NoteStatus.Idle(),
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean = status == NoteStatus.Refreshing
    val emptyError: Throwable? = (status as? NoteStatus.EmptyError)?.reason
    val isEmptyLoading: Boolean = status == NoteStatus.EmptyLoading
    val isNextPageLoading: Boolean = status == NoteStatus.NextPageLoading
}