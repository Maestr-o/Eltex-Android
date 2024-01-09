package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface EventRepository {
    fun getEvents(): Single<List<Event>>
    fun likeById(id: Long): Single<Event>
    fun unlikeById(id: Long): Single<Event>
    fun participateById(id: Long): Single<Event>
    fun unparticipateById(id: Long): Single<Event>
    fun saveEvent(id: Long, content: String): Single<Event>
    fun deleteById(id: Long): Completable
}