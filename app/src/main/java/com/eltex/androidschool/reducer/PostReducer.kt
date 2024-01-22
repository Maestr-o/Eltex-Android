package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.PostEffect
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostStatus
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.utils.Either

class PostReducer : Reducer<PostUiState, PostEffect, PostMessage> {
    override fun reduce(
        old: PostUiState,
        message: PostMessage,
    ): ReducerResult<PostUiState, PostEffect> = when (message) {
        is PostMessage.Like -> ReducerResult(
            old.copy(
                posts = old.posts.map {
                    if (it.id == message.post.id) {
                        it.copy(
                            likedByMe = !it.likedByMe,
                            likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                        )
                    } else {
                        it
                    }
                }
            ),
            PostEffect.Like(message.post)
        )

        PostMessage.LoadNextPage -> {
            val nextId = old.posts.lastOrNull()?.id
            if (nextId == null || old.isNextPageLoading) {
                ReducerResult(old)
            } else {
                ReducerResult(
                    old.copy(status = PostStatus.NextPageLoading),
                    PostEffect.LoadNextPage(nextId, 10),
                )
            }
        }

        PostMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.posts.isEmpty()) {
                    PostStatus.EmptyLoading
                } else {
                    PostStatus.Refreshing
                }
            ),
            PostEffect.LoadInitialPage(count = 15),
        )

        is PostMessage.Delete -> ReducerResult(
            old.copy(posts = old.posts.filter { it.id != message.post.id }),
            PostEffect.Delete(message.post),
        )

        is PostMessage.DeleteError -> with(message.error) {
            ReducerResult(
                old.copy(
                    posts = buildList(old.posts.size + 1) {
                        addAll(old.posts.takeWhile { it.id > postUiModel.id })
                        add(postUiModel)
                        addAll(old.posts.takeLastWhile { it.id < postUiModel.id })
                    },
                    singleError = throwable,
                )
            )
        }

        is PostMessage.NextPageLoaded -> ReducerResult(
            when (message.result) {
                is Either.Left -> {
                    old.copy(status = PostStatus.NextPageError(message.result.value))
                }

                is Either.Right -> old.copy(
                    posts = old.posts + message.result.value,
                    status = PostStatus.Idle,
                )
            }
        )

        is PostMessage.LikeResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    val value = result.value
                    val post = value.postUiModel
                    old.copy(
                        posts = old.posts.map {
                            if (it.id == post.id) {
                                post
                            } else {
                                it
                            }
                        },
                        singleError = value.throwable,
                    )
                }

                is Either.Right -> {
                    val post = result.value
                    old.copy(
                        posts = old.posts.map {
                            if (it.id == post.id) {
                                post
                            } else {
                                it
                            }
                        },
                    )
                }
            }
        )

        PostMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is PostMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    if (old.posts.isEmpty()) {
                        old.copy(status = PostStatus.EmptyError(result.value))
                    } else {
                        old.copy(singleError = result.value)
                    }
                }

                is Either.Right -> old.copy(
                    posts = result.value,
                    status = PostStatus.Idle,
                )
            }
        )
    }
}