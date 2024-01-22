package com.eltex.androidschool.effecthandler

import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.PostEffect
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostWithError
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import java.util.concurrent.CancellationException

class PostEffectHandler(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper,
) : EffectHandler<PostEffect, PostMessage> {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun connect(effects: Flow<PostEffect>): Flow<PostMessage> =
        listOf(
            effects.filterIsInstance<PostEffect.LoadInitialPage>()
                .mapLatest {
                    PostMessage.InitialLoaded(
                        try {
                            Either.Right(
                                repository.getLatest(it.count)
                                    .map(mapper::map)
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(e)
                        }
                    )
                },
            listOf(
                effects.filterIsInstance<PostEffect.LoadInitialPage>(),
                effects.filterIsInstance<PostEffect.LoadNextPage>()
            )
                .merge()
                .mapLatest {
                    if (it is PostEffect.LoadNextPage) {
                        PostMessage.NextPageLoaded(
                            try {
                                Either.Right(
                                    repository.getBefore(it.id, it.count)
                                        .map(mapper::map)
                                )
                            } catch (e: Exception) {
                                if (e is CancellationException) throw e
                                Either.Left(e)
                            }
                        )
                    } else {
                        null
                    }
                }
                .filterNotNull(),
            effects.filterIsInstance<PostEffect.Like>()
                .mapLatest {
                    PostMessage.LikeResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    if (it.post.likedByMe) {
                                        repository.unlikeById(it.post.id)
                                    } else {
                                        repository.likeById(it.post.id)
                                    }
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(PostWithError(it.post, e))
                        }
                    )
                },
            effects.filterIsInstance<PostEffect.Delete>()
                .mapLatest {
                    try {
                        repository.deleteById(it.post.id)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        PostMessage.DeleteError(PostWithError(it.post, e))
                    }
                }
                .filterIsInstance<PostMessage.DeleteError>(),
        )
            .merge()
}