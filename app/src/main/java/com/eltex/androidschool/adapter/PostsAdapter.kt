package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.utils.toast

class PostsAdapter(private val likeClickListener: (Post) -> Unit) :
    ListAdapter<Post, PostViewHolder>(PostItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val postBinding = CardPostBinding.inflate(inflater, parent, false)
        val viewHolder = PostViewHolder(postBinding)

        postBinding.like.setOnClickListener {
            likeClickListener(getItem(viewHolder.adapterPosition))
        }

        postBinding.share.setOnClickListener {
            postBinding.root.context.toast(R.string.not_implemented)
        }

        postBinding.menu.setOnClickListener {
            postBinding.root.context.toast(R.string.not_implemented)
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