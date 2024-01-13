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
            updateParticipateIcon(payload.participated)
        }
        if (payload.liked != null) {
            updateLikeIcon(payload.liked)
        }
        if (payload.participants != null) {
            updateParticipateCount(payload.participants)
        }
        if (payload.likes != null) {
            updateLikeCount(payload.likes)
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
        updateLikeIcon(event.likedByMe)
        updateParticipateIcon(event.participatedByMe)
        updateLikeCount(event.likes)
        updateParticipateCount(event.participants)
    }

    private fun updateLikeIcon(liked: Boolean) {
        if (liked) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun updateParticipateIcon(participated: Boolean) {
        if (participated) {
            binding.participate.setIconResource(R.drawable.baseline_people_24)
        } else {
            binding.participate.setIconResource(R.drawable.baseline_people_outline_24)
        }
    }

    private fun updateLikeCount(likes: Int) {
        binding.like.text = likes.toString()
    }

    private fun updateParticipateCount(participants: Int) {
        binding.participate.text = participants.toString()
    }
}