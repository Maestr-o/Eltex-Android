package com.eltex.androidschool.model

import android.net.Uri

data class FileModel(
    val uri: Uri,
    val type: AttachmentType,
)