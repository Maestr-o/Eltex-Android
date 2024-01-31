package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.NoteStatus
import com.eltex.androidschool.model.PostEffect
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.utils.Either
import javax.inject.Inject

class PostReducer @Inject constructor() : Reducer<PostUiState, PostEffect, PostMessage> {

    companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_LOAD_SIZE = 15
    }

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

        PostMessage.LoadNextPage -> if (old.status == NoteStatus.Idle()) {
            ReducerResult(
                old.copy(status = NoteStatus.NextPageLoading),
                PostEffect.LoadNextPage(old.posts.last().id, PAGE_SIZE),
            )
        } else {
            ReducerResult(old)
        }

        PostMessage.Retry -> ReducerResult(
            old.copy(status = NoteStatus.NextPageLoading),
            PostEffect.LoadNextPage(old.posts.last().id, PAGE_SIZE),
        )

        PostMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.posts.isEmpty()) {
                    NoteStatus.EmptyLoading
                } else {
                    NoteStatus.Refreshing
                }
            ),
            PostEffect.LoadInitialPage(count = INITIAL_LOAD_SIZE),
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
            when (val result = message.result) {
                is Either.Left -> {
                    old.copy(status = NoteStatus.NextPageError(result.value))
                }

                is Either.Right -> old.copy(
                    posts = old.posts + result.value,
                    status = NoteStatus.Idle(result.value.size < PAGE_SIZE),
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
                        old.copy(status = NoteStatus.EmptyError(result.value))
                    } else {
                        old.copy(singleError = result.value)
                    }
                }

                is Either.Right -> old.copy(
                    posts = result.value,
                    status = NoteStatus.Idle(result.value.size < INITIAL_LOAD_SIZE),
                )
            }
        )
    }
}