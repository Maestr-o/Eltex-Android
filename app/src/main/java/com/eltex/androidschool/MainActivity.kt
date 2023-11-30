package com.eltex.androidschool

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.adapter.PostsAdapter
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer { PostViewModel(InMemoryPostRepository()) }
            }
        }

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val adapter = PostsAdapter {
            viewModel.likeById(it.id)
        }

        binding.root.adapter = adapter
        binding.root.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.posts)
            }
            .launchIn(lifecycleScope)
    }

}