package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.NetworkPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditPostViewModel(
    private val repository: NetworkPostRepository,
) : ViewModel() {

    private val _post = MutableStateFlow(Post())
    val post: StateFlow<Post> = _post.asStateFlow()

    fun update(obj: Post) {
        _post.value = obj
    }

    fun save(content: String) {

    }
}