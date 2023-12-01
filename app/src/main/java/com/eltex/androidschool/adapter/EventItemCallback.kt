package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.Event

class EventItemCallback : ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = newItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = newItem == oldItem

    override fun getChangePayload(oldItem: Event, newItem: Event): Any? =
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