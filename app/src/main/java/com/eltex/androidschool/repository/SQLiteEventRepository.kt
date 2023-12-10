package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventsDao
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SQLiteEventRepository(
    private val dao: EventsDao
) : EventRepository {

    private val state = MutableStateFlow(readEvents())

    private fun readEvents(): List<Event> = dao.getAll()

    override fun getEvents(): Flow<List<Event>> = state.asStateFlow()

    override fun likeById(id: Long) {
        dao.likeById(id)
        state.update { readEvents() }
    }

    override fun participateById(id: Long) {
        dao.participatedById(id)
        state.update { readEvents() }
    }

    override fun addEvent(content: String) {
        dao.save(
            Event(
                content = content,
                author = "Me",
                datetime = "16.12.2023 17:00",
                published = "10.12.2023 11:24",
            )
        )
        state.update { readEvents() }
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        state.update { readEvents() }
    }

    override fun editById(id: Long, content: String) {
        dao.save(
            dao.getEventById(id).copy(
                id = id,
                content = content,
            )
        )
        state.update { readEvents() }
    }
}