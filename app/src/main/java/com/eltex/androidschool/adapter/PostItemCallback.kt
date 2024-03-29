package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.PostUiModel

class PostItemCallback : ItemCallback<PostUiModel>() {
    override fun areItemsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: PostUiModel, newItem: PostUiModel): Any? =
        PostPayload(
            liked = newItem.likedByMe.takeIf {
                it != oldItem.likedByMe
            },
            likes = newItem.likes.takeIf {
                it != oldItem.likes
            },
            content = newItem.content.takeIf {
                it != oldItem.content
            },
            attachment = newItem.attachment.takeIf {
                it != oldItem.attachment
            }
        )
            .takeIf { it.isNotEmpty() }
}