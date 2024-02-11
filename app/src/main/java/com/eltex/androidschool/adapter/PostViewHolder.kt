package com.eltex.androidschool.adapter

import android.graphics.drawable.Drawable
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.PostUiModel

class PostViewHolder(
    private val binding: CardPostBinding,
) : ViewHolder(binding.root) {

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
        binding.authorInitials.text = post.author.take(1)
        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
        updateLikeIcon(post.likedByMe)
        updateLikeCount(post.likes)
        updateAvatar(post.authorAvatar)
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
                        target: com.bumptech.glide.request.target.Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        binding.authorInitials.isGone = true
                        return false
                    }
                }
            )
            .into(binding.avatar)
    }
}