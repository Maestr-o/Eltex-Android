package com.eltex.androidschool.di

import com.eltex.androidschool.repository.EventRepository

interface EventRepositoryFactory {
    fun create(): EventRepository
}