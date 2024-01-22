package com.eltex.androidschool.utils

sealed interface Either<out Left, out Right> {
    data class Left<T>(val value: T) : Either<T, Nothing>
    data class Right<T>(val value: T) : Either<Nothing, T>
}