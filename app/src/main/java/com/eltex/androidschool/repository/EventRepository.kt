package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event

interface EventRepository {
    suspend fun getLatest(count: Int): List<Event>
    suspend fun getBefore(id: Long, count: Int): List<Event>
    suspend fun likeById(id: Long): Event
    suspend fun unlikeById(id: Long): Event
    suspend fun participateById(id: Long): Event
    suspend fun unparticipateById(id: Long): Event
    suspend fun saveEvent(id: Long, content: String): Event
    suspend fun deleteById(id: Long)
}