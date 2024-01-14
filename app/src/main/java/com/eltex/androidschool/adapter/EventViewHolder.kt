package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.EventUiModel

class EventViewHolder(
    private val binding: CardEventBinding,
) : ViewHolder(binding.root) {

    fun bindEvent(payload: EventPayload) {
        if (payload.participated != null) {
            updateParticipate(payload.participated)
        }
        if (payload.liked != null) {
            updateLike(payload.liked)
        }
    }

    fun bindEvent(event: EventUiModel) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.authorInitials.text = event.author.take(1)
        binding.eventType.text = event.type.toString()
        binding.eventTime.text = event.datetime
        binding.link.text = event.link
        updateLike(event)
        updateParticipate(event)
    }

    private fun updateLike(likedByMe: Boolean) {
        binding.like.text = if (likedByMe) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
            binding.like.text.toString().toInt() + 1
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
            binding.like.text.toString().toInt() - 1
        }.toString()
    }

    private fun updateParticipate(participatedByMe: Boolean) {
        binding.participate.text = if (participatedByMe) {
            binding.participate.setIconResource(R.drawable.baseline_people_24)
            binding.participate.text.toString().toInt() + 1
        } else {
            binding.participate.setIconResource(R.drawable.baseline_people_outline_24)
            binding.participate.text.toString().toInt() - 1
        }.toString()
    }

    private fun updateLike(event: EventUiModel) {
        if (event.likedByMe) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
        }
        binding.like.text = event.likes.toString()
    }

    private fun updateParticipate(event: EventUiModel) {
        if (event.participatedByMe) {
            binding.participate.setIconResource(R.drawable.baseline_people_24)
        } else {
            binding.participate.setIconResource(R.drawable.baseline_people_outline_24)
        }
        binding.participate.text = event.participants.toString()
    }
}