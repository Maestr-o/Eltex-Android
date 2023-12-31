package com.eltex.androidschool.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.PostsAdapter
import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.databinding.FragmentPostsBinding
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.NetworkPostRepository
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.viewmodel.EditPostViewModel
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer {
                    PostViewModel(NetworkPostRepository(PostsApi.INSTANCE))
                }
            }
        }

        val editPostViewModel by activityViewModels<EditPostViewModel> {
            viewModelFactory {
                initializer {
                    EditPostViewModel(NetworkPostRepository(PostsApi.INSTANCE))
                }
            }
        }

        val adapter = PostsAdapter(
            object : PostsAdapter.PostListener {
                override fun onLikeClickListener(post: Post) {
                    viewModel.likeById(post)
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
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(R.id.action_bottomNavigationFragment_to_editPostFragment)
                }
            }
        )

        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
        }

        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPostFragment.POST_UPDATED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.load()
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.swipeRefresh.isRefreshing = state.isRefreshing

                val emptyError = state.emptyError
                binding.errorGroup.isVisible = emptyError != null
                binding.errorText.text = emptyError?.getText(requireContext())

                binding.progress.isVisible = state.isEmptyLoading

                state.refreshingError?.let {
                    Toast.makeText(
                        requireContext(),
                        it.getText(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()

                    viewModel.consumeError()
                }

                adapter.submitList(state.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

}