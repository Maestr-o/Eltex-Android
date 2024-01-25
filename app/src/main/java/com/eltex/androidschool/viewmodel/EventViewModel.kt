package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(
    private val store: EventStore,
) : ViewModel() {
    val uiState: StateFlow<EventUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: EventMessage) = store.accept(message)
}