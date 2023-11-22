package com.eltex.androidschool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.databinding.PostCardBinding
import com.eltex.androidschool.model.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1L,
            content = "Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            likedById = false,
        )

        binding.author.text = post.author
        binding.content.text = post.content
        binding.published.text = post.published
        binding.initial.text = post.author.take(1)

        bindPost(binding, post)

        binding.like.setOnClickListener {
            post = post.copy(likedById = !post.likedById)
            bindPost(binding, post)
        }

    }

    private fun bindPost(binding: PostCardBinding, post: Post) {
        binding.like.text = if (post.likedById) {
            binding.like.setIconResource(R.drawable.baseline_favorite_24)
            1
        } else {
            binding.like.setIconResource(R.drawable.baseline_favorite_border_24)
            0
        }.toString()
    }
}