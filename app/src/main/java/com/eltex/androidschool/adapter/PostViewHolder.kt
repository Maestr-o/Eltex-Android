package com.eltex.androidschool.adapter

import android.graphics.Bitmap
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.PostUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewHolder(
    private val binding: CardPostBinding,
) : ViewHolder(binding.root) {

    private var avatarJob: Job? = null

    fun bindPost(payload: PostPayload) {
        if (payload.liked != null) {
            updateLikeIcon(payload.liked)
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

    fun bindPost(post: PostUiModel) {
        cancelAvatarLoading()
        setDefaultAvatar(post.author.take(1))
        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
        updateLikeIcon(post.likedByMe)
        updateLikeCount(post.likes)
        if (post.authorAvatar != null) {
            avatarJob = CoroutineScope(Dispatchers.Main).launch {
                updateAvatar(post.authorAvatar)
            }
        }
        if (post.attachment != null) {
            binding.image.isVisible = true
            updateAttachment(post.attachment)
        } else {
            binding.image.isGone = true
        }
    }

    private fun updateLikeIcon(liked: Boolean) {
        if (liked) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun updateLikeCount(likes: Int) {
        binding.like.text = likes.toString()
    }

    private fun updateContent(content: String) {
        binding.content.text = content
    }

    private fun updateAttachment(attachment: Attachment) {
        Glide.with(binding.image)
            .load(attachment.url)
            .into(binding.image)
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

    fun cancelAvatarLoading() {
        avatarJob?.cancel()
    }
}