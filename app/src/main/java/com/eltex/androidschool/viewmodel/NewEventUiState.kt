package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status

data class NewEventUiState(
    val result: Event? = null,
    val status: Status = Status.Idle,
    val file: FileModel? = null,
)