package com.eltex.androidschool.dao

import com.eltex.androidschool.model.Event

interface EventsDao {
    fun getAll(): List<Event>
    fun getEventById(id: Long): Event
    fun save(event: Event): Event
    fun deleteById(id: Long)
    fun likeById(id: Long): Event
    fun participatedById(id: Long): Event
}