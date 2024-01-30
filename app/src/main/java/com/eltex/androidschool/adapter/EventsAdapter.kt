package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.databinding.CardEventSkeletonBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.PagingModel

class EventsAdapter(
    private val listener: EventListener
) : ListAdapter<PagingModel<EventUiModel>, ViewHolder>(EventPagingModelItemCallback()) {

    interface EventListener {
        fun onLikeClickListener(event: EventUiModel)
        fun onParticipateClickListener(event: EventUiModel)
        fun onShareClickListener(event: EventUiModel)
        fun onDeleteClickListener(event: EventUiModel)
        fun onEditClickListener(event: EventUiModel)
        fun onRetryPageClickListener()
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PagingModel.Data -> R.layout.card_event
            is PagingModel.Error -> R.layout.item_error
            PagingModel.Progress -> R.layout.card_event_skeleton
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            R.layout.card_event -> createEventViewHolder(parent)
            R.layout.item_error -> createErrorViewHolder(parent)
            R.layout.card_event_skeleton -> createProgressViewHolder(parent)
            else -> error("Unknown view type: $viewType")
        }

    private fun createEventViewHolder(parent: ViewGroup): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val eventBinding = CardEventBinding.inflate(inflater, parent, false)
        val viewHolder = EventViewHolder(eventBinding)

        eventBinding.like.setOnClickListener {
            listener.onLikeClickListener(
                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
            )
        }

        eventBinding.participate.setOnClickListener {
            listener.onParticipateClickListener(
                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
            )
        }

        eventBinding.share.setOnClickListener {
            listener.onShareClickListener(
                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
            )
        }

        eventBinding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.note_actions_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClickListener(
                                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
                            )
                            true
                        }

                        R.id.edit -> {
                            listener.onEditClickListener(
                                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
                            )
                            true
                        }

                        else -> false
                    }
                }
                show()
            }
        }

        return viewHolder
    }

    private fun createErrorViewHolder(parent: ViewGroup): ErrorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemErrorBinding.inflate(inflater, parent, false)

        binding.retry.setOnClickListener {
            listener.onRetryPageClickListener()
        }

        return ErrorViewHolder(binding)
    }

    private fun createProgressViewHolder(parent: ViewGroup): ProgressEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardEventSkeletonBinding.inflate(inflater, parent, false)

        return ProgressEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is EventPayload && holder is EventViewHolder) {
                    holder.bindEvent(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as EventViewHolder).bindEvent(item.value)
            is PagingModel.Error -> (holder as ErrorViewHolder).bind(item.throwable)
            PagingModel.Progress -> Unit
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is EventViewHolder) {
            holder.cancelAvatarLoading()
        }
    }
}