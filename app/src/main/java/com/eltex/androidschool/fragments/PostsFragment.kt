package com.eltex.androidschool.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.adapter.PostsAdapter
import com.eltex.androidschool.databinding.FragmentPostsBinding
import com.eltex.androidschool.db.AppDb
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.SQLitePostRepository
import com.eltex.androidschool.viewmodel.EditPostViewModel
import com.eltex.androidschool.viewmodel.PostViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostsFragment : Fragment() {

    private lateinit var binding: FragmentPostsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarViewModel by activityViewModels<ToolbarViewModel>()
        toolbarViewModel.updateTitle(getString(R.string.app_name))

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer {
                    PostViewModel(
                        SQLitePostRepository(
                            AppDb.getInstance(requireContext().applicationContext).postsDao
                        )
                    )
                }
            }
        }

        val editPostViewModel by activityViewModels<EditPostViewModel> {
            viewModelFactory {
                initializer {
                    EditPostViewModel(
                        repository = SQLitePostRepository(
                            AppDb.getInstance(
                                requireContext().applicationContext
                            ).postsDao
                        ),
                    )
                }
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
                    editPostViewModel.update(post)
                    findNavController().navigate(R.id.action_postsFragment_to_editPostFragment)
                }
            }
        )

        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                adapter.submitList(it.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

}