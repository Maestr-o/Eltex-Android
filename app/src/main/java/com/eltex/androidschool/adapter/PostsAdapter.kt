package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.PostUiModel

class PostsAdapter(private val listener: PostListener) :
    ListAdapter<PostUiModel, PostViewHolder>(PostItemCallback()) {

    interface PostListener {
        fun onLikeClickListener(post: PostUiModel)
        fun onShareClickListener(post: PostUiModel)
        fun onDeleteClickListener(post: PostUiModel)
        fun onEditClickListener(post: PostUiModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val postBinding = CardPostBinding.inflate(inflater, parent, false)
        val viewHolder = PostViewHolder(postBinding)

        postBinding.like.setOnClickListener {
            listener.onLikeClickListener(getItem(viewHolder.adapterPosition))
        }

        postBinding.share.setOnClickListener {
            listener.onShareClickListener(getItem(viewHolder.adapterPosition))
        }

        postBinding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.note_actions_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClickListener(getItem(viewHolder.adapterPosition))
                            true
                        }

                        R.id.edit -> {
                            listener.onEditClickListener(getItem(viewHolder.adapterPosition))
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

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is PostPayload) {
                    holder.bindPost(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindPost(getItem(position))
    }
}