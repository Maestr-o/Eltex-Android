package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventsDao
import com.eltex.androidschool.entity.EventEntity
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SQLiteEventRepository(private val dao: EventsDao) : EventRepository {

    override fun getEvents(): Flow<List<Event>> = dao.getAll().map {
        it.map(EventEntity::toEvent)
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun participateById(id: Long) {
        dao.participatedById(id)
    }

    override fun saveEvent(id: Long, content: String) {
        val event = dao.getEventById(id)
        if (event == null) {
            dao.save(
                EventEntity.fromEvent(
                    Event(
                        id = id,
                        content = content,
                        author = "Me",
                        published = "13.12.2023 20:50",
                        datetime = "24.12.2023 14:00",
                        link = "qwerty.com",
                    )
                )
            )
        } else {
            dao.save(event.copy(content = content))
        }
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

}