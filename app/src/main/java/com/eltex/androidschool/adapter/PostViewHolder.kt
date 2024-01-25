package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
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
    }

    fun bindPost(post: PostUiModel) {
        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
        binding.authorInitials.text = post.author.take(1)
        updateLikeIcon(post.likedByMe)
        updateLikeCount(post.likes)
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
}