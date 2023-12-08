package com.eltex.androidschool.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val lat: Double = 0.0,
    val long: Double = 0.0,
)