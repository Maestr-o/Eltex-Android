package com.eltex.androidschool.model

import com.eltex.androidschool.utils.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Post(
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
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("coords")
    val coords: Coordinates? = null,
    @SerialName("link")
    val link: String? = null,
    @SerialName("mentionedMe")
    val mentionedMe: Boolean = false,
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("attachment")
    val attachment: Attachment? = null,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
)