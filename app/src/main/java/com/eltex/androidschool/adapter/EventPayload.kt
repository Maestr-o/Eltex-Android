package com.eltex.androidschool.adapter

data class EventPayload(
    val participated: Boolean? = null,
    val liked: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = ((participated != null) || (liked != null))
}