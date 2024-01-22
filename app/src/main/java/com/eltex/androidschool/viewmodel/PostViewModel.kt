package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val store: PostStore,
) : ViewModel() {
    val uiState: StateFlow<PostUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: PostMessage) = store.accept(message)
}