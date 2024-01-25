package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.PagingModel

class EventPagingModelItemCallback : ItemCallback<PagingModel<EventUiModel>>() {

    private val eventItemCallback = EventItemCallback()

    override fun areItemsTheSame(
        oldItem: PagingModel<EventUiModel>,
        newItem: PagingModel<EventUiModel>
    ): Boolean {
        if (oldItem::class != newItem::class) return false

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            eventItemCallback.areItemsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: PagingModel<EventUiModel>,
        newItem: PagingModel<EventUiModel>
    ): Boolean {
        if (oldItem::class != newItem::class) return false

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            eventItemCallback.areContentsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun getChangePayload(
        oldItem: PagingModel<EventUiModel>,
        newItem: PagingModel<EventUiModel>
    ): Any? {
        if (oldItem::class != newItem::class) return null

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            eventItemCallback.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}