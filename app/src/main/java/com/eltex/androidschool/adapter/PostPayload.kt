package com.eltex.androidschool.adapter

data class PostPayload(
    val liked: Boolean? = null
) {
    fun isNotEmpty(): Boolean = liked != null
}