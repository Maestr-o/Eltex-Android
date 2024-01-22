package com.eltex.androidschool.model

data class PostUiState(
    val posts: List<PostUiModel> = emptyList(),
    val status: PostStatus = PostStatus.Idle,
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean = status == PostStatus.Refreshing
    val emptyError: Throwable? = (status as? PostStatus.EmptyError)?.reason
    val isEmptyLoading: Boolean = status == PostStatus.EmptyLoading
    val isNextPageLoading: Boolean = status == PostStatus.NextPageLoading
}