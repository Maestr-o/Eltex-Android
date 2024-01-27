package com.eltex.androidschool.adapter

import android.graphics.Bitmap
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.EventUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        if (payload.content != null) {
            updateContent(payload.content)
        }
    }

    fun bindEvent(event: EventUiModel) {
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.eventType.text = event.type.toString()
        binding.eventTime.text = event.datetime
        binding.link.text = event.link
        setDefaultAvatar(event.author.take(1))
        if (event.authorAvatar != null) {
            CoroutineScope(Dispatchers.Main).launch {
                updateAvatar(event.authorAvatar)
            }
        }
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

    private fun updateContent(content: String) {
        binding.content.text = content
    }

    private fun setDefaultAvatar(letter: String) {
        binding.authorInitials.isVisible = true
        binding.authorInitials.text = letter
        Glide.with(binding.avatar)
            .load(R.drawable.avatar_background)
            .into(binding.avatar)
    }

    private suspend fun updateAvatar(avatar: String) = withContext(Dispatchers.IO) {
        try {
            val avatarBitmap: Bitmap = Glide.with(binding.avatar)
                .asBitmap()
                .load(avatar)
                .submit()
                .get()

            withContext(Dispatchers.Main) {
                Glide.with(binding.avatar)
                    .load(avatarBitmap)
                    .circleCrop()
                    .into(binding.avatar)
                binding.authorInitials.isGone = true
            }
        } catch (_: Exception) {
        }
    }
}