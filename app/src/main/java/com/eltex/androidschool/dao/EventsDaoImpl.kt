package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import com.eltex.androidschool.db.EventTable
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow

class EventsDaoImpl(private val db: SQLiteDatabase) : EventsDao {
    override fun getAll(): List<Event> =
        db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns,
            null,
            null,
            null,
            null,
            "${EventTable.ID} DESC"
        ).use { cursor ->
            val result = mutableListOf<Event>()
            while (cursor.moveToNext()) {
                result += cursor.getEvent()
            }
            result
        }

    override fun save(event: Event): Event {
        val contentValues = contentValuesOf(
            EventTable.AUTHOR to event.author,
            EventTable.CONTENT to event.content,
            EventTable.DATETIME to event.datetime,
            EventTable.PUBLISHED to event.published,
            EventTable.LIKED_BY_ME to event.likedByMe,
            EventTable.PARTICIPATED_BY_ME to event.participatedByMe
        )

        if (event.id != 0L) {
            contentValues.put(EventTable.ID, event.id)
        }

        val id = db.replace(EventTable.TABLE_NAME, null, contentValues)
        return getEventById(id)
    }

    override fun deleteById(id: Long) {
        db.delete(EventTable.TABLE_NAME, "${EventTable.ID} = ?", arrayOf(id.toString()))
    }

    override fun likeById(id: Long): Event {
        db.execSQL(
            """
            UPDATE ${EventTable.TABLE_NAME} SET 
                ${EventTable.LIKED_BY_ME} = CASE WHEN ${EventTable.LIKED_BY_ME} THEN 0 ELSE 1 END 
            WHERE id = ?;
            """.trimIndent(),
            arrayOf(id.toString())
        )
        return getEventById(id)
    }

    override fun participatedById(id: Long): Event {
        db.execSQL(
            """
            UPDATE ${EventTable.TABLE_NAME} SET 
                ${EventTable.PARTICIPATED_BY_ME} = CASE
                WHEN ${EventTable.PARTICIPATED_BY_ME} THEN 0 ELSE 1 END 
            WHERE id = ?;
            """.trimIndent(),
            arrayOf(id.toString())
        )
        return getEventById(id)
    }

    override fun getEventById(id: Long): Event =
        db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns,
            "${EventTable.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.getEvent()
        }

    private fun Cursor.getEvent(): Event =
        Event(
            id = getLongOrThrow(EventTable.ID),
            author = getStringOrThrow(EventTable.AUTHOR),
            content = getStringOrThrow(EventTable.CONTENT),
            datetime = getStringOrThrow(EventTable.DATETIME),
            published = getStringOrThrow(EventTable.PUBLISHED),
            likedByMe = getBooleanOrThrow(EventTable.LIKED_BY_ME),
            participatedByMe = getBooleanOrThrow(EventTable.PARTICIPATED_BY_ME),
        )
}
