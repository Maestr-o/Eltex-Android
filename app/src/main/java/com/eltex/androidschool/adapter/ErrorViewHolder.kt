package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.utils.getText

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : ViewHolder(binding.root) {

    fun bind(throwable: Throwable) {
        binding.errorText.text = throwable.getText(binding.root.context)
    }
}