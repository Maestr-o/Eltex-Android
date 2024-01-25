package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.databinding.CardPostSkeletonBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.PostUiModel

class PostsAdapter(
    private val listener: PostListener
) : ListAdapter<PagingModel<PostUiModel>, ViewHolder>(PostPagingModelItemCallback()) {

    interface PostListener {
        fun onLikeClickListener(post: PostUiModel)
        fun onShareClickListener(post: PostUiModel)
        fun onDeleteClickListener(post: PostUiModel)
        fun onEditClickListener(post: PostUiModel)
        fun onRetryPageClickListener()
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PagingModel.Data -> R.layout.card_post
            is PagingModel.Error -> R.layout.item_error
            PagingModel.Progress -> R.layout.card_post_skeleton
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            R.layout.card_post -> createPostViewHolder(parent)
            R.layout.item_error -> createErrorViewHolder(parent)
            R.layout.card_post_skeleton -> createProgressViewHolder(parent)
            else -> error("Unknown view type: $viewType")
        }

    private fun createPostViewHolder(parent: ViewGroup): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val postBinding = CardPostBinding.inflate(inflater, parent, false)
        val viewHolder = PostViewHolder(postBinding)

        postBinding.like.setOnClickListener {
            listener.onLikeClickListener(
                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
            )
        }

        postBinding.share.setOnClickListener {
            listener.onShareClickListener(
                (getItem(viewHolder.adapterPosition) as PagingModel.Data).value
            )
        }

        postBinding.menu.setOnClickListener {
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

    private fun createProgressViewHolder(parent: ViewGroup): ProgressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardPostSkeletonBinding.inflate(inflater, parent, false)

        return ProgressViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is PostPayload && holder is PostViewHolder) {
                    holder.bindPost(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as PostViewHolder).bindPost(item.value)
            is PagingModel.Error -> (holder as ErrorViewHolder).bind(item.throwable)
            PagingModel.Progress -> Unit
        }
    }
}