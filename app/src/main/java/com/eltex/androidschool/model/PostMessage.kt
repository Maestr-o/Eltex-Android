package com.eltex.androidschool.model

import com.eltex.androidschool.utils.Either

sealed interface PostMessage {
    // => к серверу
    data object LoadNextPage : PostMessage
    data object Refresh : PostMessage
    data object Retry : PostMessage
    data class Like(val post: PostUiModel) : PostMessage
    data class Delete(val post: PostUiModel) : PostMessage
    data object HandleError : PostMessage

    // <= от сервера
    data class DeleteError(val error: PostWithError) : PostMessage
    data class LikeResult(val result: Either<PostWithError, PostUiModel>) : PostMessage
    data class InitialLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
    data class NextPageLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
}