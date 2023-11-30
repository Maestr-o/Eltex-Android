package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.Post

class PostViewHolder(
    private val binding: CardPostBinding,
) : ViewHolder(binding.root) {

    fun bindPost(payload: PostPayload) {
        if (payload.liked != null) {
            updateLike(payload.liked)
        }
    }

    fun bindPost(post: Post) {
        binding.content.text = post.content
        binding.author.text = post.author
        binding.published.text = post.published
        binding.authorInitials.text = post.author.take(1)
        updateLike(post.likedByMe)
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

}