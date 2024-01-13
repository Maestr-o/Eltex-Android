package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.time.Instant

class NetworkEventRepository(
    private val api: EventsApi
) : EventRepository {

    override fun getEvents(): Single<List<Event>> = api.getAll()

    override fun likeById(id: Long): Single<Event> = api.like(id)

    override fun unlikeById(id: Long): Single<Event> = api.unlike(id)

    override fun participateById(id: Long): Single<Event> = api.participate(id)

    override fun unparticipateById(id: Long): Single<Event> = api.unparticipate(id)

    override fun saveEvent(id: Long, content: String): Single<Event> = api.save(
        Event(
            id = id,
            content = content,
            datetime = Instant.now(),
        )
    )

    override fun deleteById(id: Long): Completable = api.delete(id)
}