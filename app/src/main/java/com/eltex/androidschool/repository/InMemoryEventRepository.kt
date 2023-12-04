package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryEventRepository : EventRepository {
    private val state = MutableStateFlow(
        List(20) {
            Event(
                id = (it + 1).toLong(),
                authorId = 1L,
                author = "Евгений Евгеньевич",
                authorJob = "Eltex",
                content = "${it + 1}. Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                datetime = "10.12.2023 15:00",
                published = "25.11.2023 00:29",
                coords = Coordinates(lat = 55.05, long = 83.44),
                type = EventType.OFFLINE,
                link = "qwerty.com",
            )
        }
            .reversed()
    )

    private var nextId = state.value.first().id

    override fun getEvents(): Flow<List<Event>> = state.asStateFlow()

    override fun likeById(id: Long) {
        state.update {
            it.map { event ->
                if (event.id == id) {
                    event.copy(likedByMe = !event.likedByMe)
                } else
                    event
            }
        }
    }

    override fun participateById(id: Long) {
        state.update {
            it.map { event ->
                if (event.id == id) {
                    event.copy(participatedByMe = !event.participatedByMe)
                } else
                    event
            }
        }
    }

    override fun addEvent(content: String) {
        state.update { events ->
            buildList(events.size + 1) {
                add(
                    Event(
                        id = ++nextId,
                        content = content,
                        author = "Me",
                        published = "11.12.2023 10:34",
                        datetime = "10.12.2023 15:00",
                        link = "qwerty.com",
                    )
                )
                addAll(events)
            }
        }
    }

    override fun deleteById(id: Long) {
        state.update { events ->
            events.filter {
                it.id != id
            }
        }
    }

    override fun editById(id: Long, content: String) {
        state.update { events ->
            events.map {
                if (it.id == id) {
                    it.copy(content = content)
                } else
                    it
            }
        }
    }
}