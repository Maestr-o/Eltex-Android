package com.eltex.androidschool.effecthandler

import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventEffect
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventWithError
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import java.util.concurrent.CancellationException

class EventEffectHandler(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
) : EffectHandler<EventEffect, EventMessage> {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun connect(effects: Flow<EventEffect>): Flow<EventMessage> =
        listOf(
            effects.filterIsInstance<EventEffect.LoadInitialPage>()
                .mapLatest {
                    EventMessage.InitialLoaded(
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
                effects.filterIsInstance<EventEffect.LoadInitialPage>(),
                effects.filterIsInstance<EventEffect.LoadNextPage>()
            )
                .merge()
                .mapLatest {
                    if (it is EventEffect.LoadNextPage) {
                        EventMessage.NextPageLoaded(
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
            effects.filterIsInstance<EventEffect.Like>()
                .mapLatest {
                    EventMessage.LikeResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    if (it.event.likedByMe) {
                                        repository.unlikeById(it.event.id)
                                    } else {
                                        repository.likeById(it.event.id)
                                    }
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(EventWithError(it.event, e))
                        }
                    )
                },
            effects.filterIsInstance<EventEffect.Participate>()
                .mapLatest {
                    EventMessage.ParticipateResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    if (it.event.participatedByMe) {
                                        repository.unparticipateById(it.event.id)
                                    } else {
                                        repository.participateById(it.event.id)
                                    }
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(EventWithError(it.event, e))
                        }
                    )
                },
            effects.filterIsInstance<EventEffect.Delete>()
                .mapLatest {
                    try {
                        repository.deleteById(it.event.id)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        EventMessage.DeleteError(EventWithError(it.event, e))
                    }
                }
                .filterIsInstance<EventMessage.DeleteError>(),
        )
            .merge()
}