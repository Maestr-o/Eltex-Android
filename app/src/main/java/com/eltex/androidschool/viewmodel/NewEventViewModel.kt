package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.repository.SQLiteEventRepository

class NewEventViewModel(
    private val repository: SQLiteEventRepository,
    private val id: Long,
) : ViewModel() {

    fun save(content: String) {
        repository.saveEvent(id, content)
    }
}