package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.EventUiModel

class EventItemCallback : ItemCallback<EventUiModel>() {
    override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
        newItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
        newItem == oldItem

    override fun getChangePayload(oldItem: EventUiModel, newItem: EventUiModel): Any? =
        EventPayload(
            participated = newItem.participatedByMe.takeIf {
                it != oldItem.participatedByMe
            },
            liked = newItem.likedByMe.takeIf {
                it != oldItem.likedByMe
            },
        )
            .takeIf {
                it.isNotEmpty()
            }
}