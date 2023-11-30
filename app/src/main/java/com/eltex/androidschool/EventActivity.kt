package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = CardEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer { EventViewModel(InMemoryEventRepository()) }
            }
        }

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { bindEvent(binding, it.event) }
            .launchIn(lifecycleScope)

        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.participate.setOnClickListener {
            viewModel.participate()
        }

        binding.share.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }

        binding.menu.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }
    }

    private fun bindEvent(binding: CardEventBinding, event: Event) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.authorInitials.text = event.author.take(1)
        binding.eventType.text = event.type.toString()
        binding.eventTime.text = event.datetime
        binding.link.text = event.link
        binding.like.text = if (event.likedByMe) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
            1
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
            0
        }.toString()
        binding.participate.text = if (event.participatedByMe) {
            binding.participate.setIconResource(R.drawable.baseline_people_24)
            1
        } else {
            binding.participate.setIconResource(R.drawable.baseline_people_outline_24)
            0
        }.toString()
    }
}