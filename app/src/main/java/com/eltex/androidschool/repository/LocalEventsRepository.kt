package com.eltex.androidschool.repository

import android.content.Context
import androidx.core.content.edit
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalEventsRepository(
    private val context: Context
) : EventRepository {

    companion object {
        const val EVENTS_FILENAME = "EVENTS_FILENAME"
        const val ID_FILENAME = "ID_FILENAME"
    }

    private val preferences = context.getSharedPreferences(ID_FILENAME, Context.MODE_PRIVATE)
    private val state = MutableStateFlow(readEvents())
    private var nextId = readId()

    private fun readEvents(): List<Event> {
        val file = context.filesDir.resolve(EVENTS_FILENAME)
        val serializedEvents = if (file.exists()) {
            file.bufferedReader()
                .use {
                    it.readLine()
                }
        } else {
            null
        }

        return if (serializedEvents != null) {
            Json.decodeFromString(serializedEvents)
        } else {
            emptyList()
        }
    }

    private fun readId(): Long = preferences.getLong(ID_FILENAME, 0L)

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
        sync()
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
        sync()
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
        sync()
    }

    override fun deleteById(id: Long) {
        state.update { events ->
            events.filter {
                it.id != id
            }
        }
        sync()
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
        sync()
    }

    private fun sync() {
        preferences.edit {
            putLong(ID_FILENAME, nextId)
        }
        context.filesDir.resolve(EVENTS_FILENAME)
            .bufferedWriter()
            .use {
                it.write(Json.encodeToString(state.value))
            }
    }
}