package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.databinding.PostCardBinding
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer { PostViewModel(InMemoryPostRepository()) }
            }
        }

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { bindPost(binding, it.post) }
            .launchIn(lifecycleScope)

        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }

        binding.menu.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }
    }

    private fun bindPost(binding: PostCardBinding, post: Post) {
        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
        binding.authorInitials.text = post.author.take(1)
        binding.like.text = if (post.likedByMe) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
            1
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
            0
        }.toString()
    }
}