package com.eltex.androidschool.adapter

import com.eltex.androidschool.model.Attachment

data class EventPayload(
    val participated: Boolean? = null,
    val participants: Int? = null,
    val liked: Boolean? = null,
    val likes: Int? = null,
    val content: String? = null,
    val attachment: Attachment? = null,
) {
    fun isNotEmpty(): Boolean =
        participated != null || liked != null || likes != null || participants != null ||
                content != null || attachment != null
}