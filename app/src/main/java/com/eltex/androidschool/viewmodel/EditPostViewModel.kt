package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.SQLitePostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditPostViewModel(
    private val repository: SQLitePostRepository,
) : ViewModel() {

    private val _post = MutableStateFlow(Post())
    val post: StateFlow<Post> = _post.asStateFlow()

    fun update(obj: Post) {
        _post.value = obj
    }

    fun save(content: String) {
        repository.savePost(post.value.id, content)
    }
}