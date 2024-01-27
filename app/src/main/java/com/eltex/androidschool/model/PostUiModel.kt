package com.eltex.androidschool.model

data class PostUiModel(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String = "",
    val published: String = "",
    val attachment: Attachment? = null,
    val link: String? = null,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)