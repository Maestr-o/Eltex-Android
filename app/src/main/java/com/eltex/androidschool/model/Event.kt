package com.eltex.androidschool.model

import com.eltex.androidschool.utils.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("authorId")
    val authorId: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("authorJob")
    val authorJob: String? = null,
    @SerialName("authorAvatar")
    val authorAvatar: String? = null,
    @SerialName("content")
    val content: String = "",
    @Serializable(InstantSerializer::class)
    @SerialName("datetime")
    val datetime: Instant = Instant.now(),
    @Serializable(InstantSerializer::class)
    @SerialName("published")
    val published: Instant = Instant.now(),
    @SerialName("coords")
    val coords: Coordinates? = null,
    @SerialName("attachment")
    val attachment: Attachment? = null,
    @SerialName("type")
    val type: EventType = EventType.OFFLINE,
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
    @SerialName("link")
    val link: String? = null,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("participantsIds")
    val participantsIds: Set<Long> = emptySet(),
)