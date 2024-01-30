package com.eltex.androidschool.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.onScrollToBottom(crossinline action: () -> Unit) =
    addOnChildAttachStateChangeListener(
        object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                val position = getChildAdapterPosition(view)
                val count = adapter?.itemCount ?: return
                if (position != count - 1) return

                action()
            }

            override fun onChildViewDetachedFromWindow(view: View) = Unit
        }
    )