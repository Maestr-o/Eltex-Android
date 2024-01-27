package com.eltex.androidschool.adapter

import com.eltex.androidschool.model.Attachment

data class PostPayload(
    val liked: Boolean? = null,
    val likes: Int? = null,
    val content: String? = null,
    val attachment: Attachment? = null,
) {
    fun isNotEmpty(): Boolean =
        liked != null || likes != null || content != null || attachment != null
}