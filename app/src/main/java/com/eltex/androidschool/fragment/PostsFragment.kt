package com.eltex.androidschool.fragment

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
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.PostsAdapter
import com.eltex.androidschool.databinding.FragmentPostsBinding
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.mapper.PostPagingModelMapper
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.utils.getDependencyContainer
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.onScrollToBottom
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
            getDependencyContainer().getPostViewModelFactory()
        }

        val editPostViewModel by activityViewModels<EditPostViewModel> {
            getDependencyContainer().getEditPostViewModelFactory()
        }

        val adapter = PostsAdapter(
            object : PostsAdapter.PostListener {
                override fun onLikeClickListener(post: PostUiModel) {
                    viewModel.accept(PostMessage.Like(post))
                }

                override fun onShareClickListener(post: PostUiModel) {
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

                override fun onDeleteClickListener(post: PostUiModel) {
                    viewModel.accept(PostMessage.Delete(post))
                }

                override fun onEditClickListener(post: PostUiModel) {
                    editPostViewModel.update(post)
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(R.id.action_bottomNavigationFragment_to_editPostFragment)
                }

                override fun onRetryPageClickListener() {
                    viewModel.accept(PostMessage.Retry)
                }
            }
        )

        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(PostMessage.Refresh)
        }

        binding.retryButton.setOnClickListener {
            viewModel.accept(PostMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPostFragment.POST_UPDATED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(PostMessage.Refresh)
        }

        binding.list.onScrollToBottom {
            viewModel.accept(PostMessage.LoadNextPage)
        }

        val mapper = PostPagingModelMapper()

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.swipeRefresh.isRefreshing = state.isRefreshing
                val emptyError = state.emptyError
                binding.errorGroup.isVisible = emptyError != null
                binding.errorText.text = emptyError?.getText(requireContext())
                state.singleError?.let {
                    Toast.makeText(
                        requireContext(),
                        it.getText(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.accept(PostMessage.HandleError)
                }
                adapter.submitList(mapper.map(state))
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}