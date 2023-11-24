package com.eltex.androidschool.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
) : Parcelable