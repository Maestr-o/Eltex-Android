package com.eltex.androidschool.model

sealed interface NoteStatus {
    data class Idle(val finished: Boolean = false) : NoteStatus
    data object NextPageLoading : NoteStatus
    data object EmptyLoading : NoteStatus
    data object Refreshing : NoteStatus
    data class EmptyError(val reason: Throwable) : NoteStatus
    data class NextPageError(val reason: Throwable) : NoteStatus
}