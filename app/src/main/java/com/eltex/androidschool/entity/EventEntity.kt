package com.eltex.androidschool.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventType

@Entity(tableName = "Events")
data class EventEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "authorId")
    val authorId: Long = 0L,
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "authorJob")
    val authorJob: String? = null,
    @ColumnInfo(name = "authorAvatar")
    val authorAvatar: String? = null,
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "datetime")
    val datetime: String = "",
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "type")
    val type: EventType = EventType.OFFLINE,
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "participatedByMe")
    val participatedByMe: Boolean = false,
    @ColumnInfo(name = "link")
    val link: String? = null,
) {
    companion object {
        fun fromEvent(event: Event): EventEntity = with(event) {
            EventEntity(
                id = id,
                authorId = authorId,
                author = author,
                authorJob = authorJob,
                authorAvatar = authorAvatar,
                content = content,
                datetime = datetime,
                published = published,
                type = type,
                likedByMe = likedByMe,
                participatedByMe = participatedByMe,
                link = link,
            )
        }
    }

    fun toEvent(): Event = Event(
        id = id,
        authorId = authorId,
        author = author,
        authorJob = authorJob,
        authorAvatar = authorAvatar,
        content = content,
        datetime = datetime,
        published = published,
        type = type,
        likedByMe = likedByMe,
        participatedByMe = participatedByMe,
        link = link,
    )
}