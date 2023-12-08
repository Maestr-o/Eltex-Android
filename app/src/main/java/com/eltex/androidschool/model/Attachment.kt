package com.eltex.androidschool.model

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val url: String = "",
    val type: AttachmentType = AttachmentType.IMAGE,
)