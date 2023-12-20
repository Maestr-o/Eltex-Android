package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToolbarViewModel : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _showSave = MutableStateFlow(false)
    val showSave = _showSave.asStateFlow()

    private val _saveClicked = MutableStateFlow(false)
    val saveClicked = _saveClicked.asStateFlow()

    fun updateTitle(text: String) {
        _title.value = text
    }

    fun showSave(show: Boolean) {
        _showSave.value = show
    }

    fun saveClicked(pending: Boolean) {
        _saveClicked.value = pending
    }
}