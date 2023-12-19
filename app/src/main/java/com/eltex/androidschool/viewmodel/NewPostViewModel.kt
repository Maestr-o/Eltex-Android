package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.repository.SQLitePostRepository

class NewPostViewModel(
    private val repository: SQLitePostRepository,
    private val id: Long,
) : ViewModel() {

    fun save(content: String) {
        repository.savePost(id, content)
    }
}