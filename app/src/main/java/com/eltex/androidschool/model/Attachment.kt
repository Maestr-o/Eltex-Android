package com.eltex.androidschool.model

data class Attachment(
    val url: String = "",
    val type: AttachmentType = AttachmentType.IMAGE,
)