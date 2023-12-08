package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.adapter.PostsAdapter
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.FilePostRepository
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EDITED_POST_ID = "EXTRA_EDITED_POST_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer { PostViewModel(FilePostRepository(applicationContext)) }
            }
        }

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val newPostContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
                if (content != null) {
                    viewModel.addPost(content)
                }
            }

        val editPostContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val id = it.data?.getLongExtra(EXTRA_EDITED_POST_ID, 0)
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
                if (content != null && id != null) {
                    viewModel.editById(id, content)
                }
            }

        binding.newCard.setOnClickListener {
            newPostContract.launch(Intent(this, NewPostActivity::class.java))
        }

        if (intent.action == Intent.ACTION_SEND) {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            intent.removeExtra(Intent.EXTRA_TEXT)
            if (!text.isNullOrBlank()) {
                newPostContract.launch(
                    Intent(this, NewPostActivity::class.java)
                        .putExtra(Intent.EXTRA_TEXT, text)
                )
            }
        }

        val adapter = PostsAdapter(
            object : PostsAdapter.PostListener {
                override fun onLikeClickListener(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShareClickListener(post: Post) {
                    val intentShare = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(
                            Intent.EXTRA_TEXT,
                            getString(R.string.share_text, post.author, post.content)
                        )
                        .setType("text/plain")

                    val chooser = Intent.createChooser(intentShare, null)
                    startActivity(chooser)
                }

                override fun onDeleteClickListener(post: Post) {
                    viewModel.deleteById(post.id)
                }

                override fun onEditClickListener(post: Post) {
                    Intent(binding.root.context, EditPostActivity::class.java).apply {
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        putExtra(EXTRA_EDITED_POST_ID, post.id)
                        editPostContract.launch(this)
                    }
                }
            }
        )

        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.posts)
            }
            .launchIn(lifecycleScope)
    }

}