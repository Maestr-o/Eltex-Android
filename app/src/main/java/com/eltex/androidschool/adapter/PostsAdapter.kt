package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.Post

class PostsAdapter(private val likeClickListener: (Post) -> Unit) :
    ListAdapter<Post, PostViewHolder>(PostItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val postBinding = CardPostBinding.inflate(inflater, parent, false)
        val viewHolder = PostViewHolder(postBinding)

        postBinding.like.setOnClickListener {
            likeClickListener(getItem(viewHolder.adapterPosition))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindPost(getItem(position))
    }
}