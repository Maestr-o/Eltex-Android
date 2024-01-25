package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.PostUiModel

class PostPagingModelItemCallback : ItemCallback<PagingModel<PostUiModel>>() {

    private val postItemCallback = PostItemCallback()

    override fun areItemsTheSame(
        oldItem: PagingModel<PostUiModel>,
        newItem: PagingModel<PostUiModel>
    ): Boolean {
        if (oldItem::class != newItem::class) return false

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            postItemCallback.areItemsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: PagingModel<PostUiModel>,
        newItem: PagingModel<PostUiModel>
    ): Boolean {
        if (oldItem::class != newItem::class) return false

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            postItemCallback.areContentsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun getChangePayload(
        oldItem: PagingModel<PostUiModel>,
        newItem: PagingModel<PostUiModel>
    ): Any? {
        if (oldItem::class != newItem::class) return null

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            postItemCallback.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}