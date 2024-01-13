package com.eltex.androidschool.adapter

data class PostPayload(
    val liked: Boolean? = null,
    val likes: Int? = null,
) {
    fun isNotEmpty(): Boolean = liked != null || likes != null
}