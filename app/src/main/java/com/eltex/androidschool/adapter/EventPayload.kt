package com.eltex.androidschool.adapter

data class EventPayload(
    val participated: Boolean? = null,
    val participants: Int? = null,
    val liked: Boolean? = null,
    val likes: Int? = null,
) {
    fun isNotEmpty(): Boolean =
        ((participated != null) || (liked != null) || (likes != null) || (participants != null))
}