package com.eltex.androidschool.model

import java.io.Serializable

data class EventUiModel(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = "",
    val published: String = "",
    val type: EventType = EventType.OFFLINE,
    val attachment: Attachment? = null,
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val link: String? = null,
    val likes: Int = 0,
    val participants: Int = 0,
) : Serializable