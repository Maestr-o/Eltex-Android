package com.eltex.androidschool.model

sealed interface PagingModel<out T> {
    data class Data<T>(val value: T) : PagingModel<T>
    data object Progress : PagingModel<Nothing>
    data class Error(val throwable: Throwable) : PagingModel<Nothing>
}