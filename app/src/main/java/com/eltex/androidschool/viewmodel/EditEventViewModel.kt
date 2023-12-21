package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.SQLiteEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditEventViewModel(
    private val repository: SQLiteEventRepository,
) : ViewModel() {

    private val _event = MutableStateFlow(Event())
    val event: StateFlow<Event> = _event.asStateFlow()

    fun update(obj: Event) {
        _event.value = obj
    }

    fun save(content: String) {
        repository.saveEvent(event.value.id, content)
    }
}