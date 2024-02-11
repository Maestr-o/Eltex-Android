package com.eltex.androidschool.adapter

import android.graphics.drawable.Drawable
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Attachment
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
        if (payload.content != null) {
            updateContent(payload.content)
        }
        if (payload.attachment != null) {
            updateAttachment(payload.attachment)
        }
    }

    fun bindEvent(event: EventUiModel) {
        binding.authorInitials.text = event.author.take(1)
        binding.content.text = event.content
        binding.author.text = event.author
        binding.published.text = event.published
        binding.eventType.text = event.type.toString()
        binding.eventTime.text = event.datetime
        setLink(event.link)
        updateLikeIcon(event.likedByMe)
        updateParticipateIcon(event.participatedByMe)
        updateLikeCount(event.likes)
        updateParticipateCount(event.participants)
        updateAvatar(event.authorAvatar)
        if (event.attachment != null) {
            binding.image.isVisible = true
            updateAttachment(event.attachment)
        } else {
            binding.image.isGone = true
        }
    }

    private fun setLink(link: String?) {
        if (link.isNullOrBlank()) {
            binding.link.isGone = true
        } else {
            binding.link.isVisible = true
            binding.link.text = link
        }
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

    private fun updateAttachment(attachment: Attachment) {
        Glide.with(binding.image)
            .load(attachment.url)
            .into(binding.image)
    }

    private fun updateAvatar(avatar: String?) {
        binding.authorInitials.isVisible = true
        Glide.with(binding.avatar)
            .load(avatar)
            .circleCrop()
            .addListener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.authorInitials.isGone = true
                        return false
                    }
                }
            )
            .into(binding.avatar)
    }
}