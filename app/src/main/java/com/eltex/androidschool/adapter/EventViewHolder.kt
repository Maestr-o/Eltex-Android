package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event

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

    fun bindEvent(event: Event) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.authorInitials.text = event.author.take(1)
        binding.eventType.text = event.type.toString()
        binding.eventTime.text = event.datetime
        binding.link.text = event.link
        updateLike(event.likedByMe)
        updateParticipate(event.participatedByMe)
    }

    private fun updateLike(likedByMe: Boolean) {
        binding.like.text = if (likedByMe) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
            1
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
            0
        }.toString()
    }

    private fun updateParticipate(participatedByMe: Boolean) {
        binding.participate.text = if (participatedByMe) {
            binding.participate.setIconResource(R.drawable.baseline_people_24)
            1
        } else {
            binding.participate.setIconResource(R.drawable.baseline_people_outline_24)
            0
        }.toString()
    }
}