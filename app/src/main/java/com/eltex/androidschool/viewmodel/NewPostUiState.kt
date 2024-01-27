package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status

data class NewPostUiState(
    val result: Post? = null,
    val status: Status = Status.Idle,
    val file: FileModel? = null,
)