package com.eltex.androidschool.di

import com.eltex.androidschool.repository.PostRepository

interface PostRepositoryFactory {
    fun create(): PostRepository
}