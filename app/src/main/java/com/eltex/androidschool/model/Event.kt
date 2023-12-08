package com.eltex.androidschool.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = "",
    val published: String = "",
    val coords: Coordinates? = null,
    val type: EventType = EventType.OFFLINE,
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val link: String? = null,
)