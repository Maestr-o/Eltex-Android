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
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.viewmodel.EditEventViewModel
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(NetworkEventRepository())
                }
            }
        }

        val editEventViewModel by activityViewModels<EditEventViewModel> {
            viewModelFactory {
                initializer {
                    EditEventViewModel(NetworkEventRepository())
                }
            }
        }

        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: Event) {
                    viewModel.likeById(event)
                }

                override fun onParticipateClickListener(event: Event) {
                    viewModel.participateById(event)
                }

                override fun onShareClickListener(event: Event) {
                    val intentShare = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(
                            Intent.EXTRA_TEXT,
                            getString(R.string.share_text, event.author, event.content)
                        )
                        .setType("text/plain")

                    val chooser = Intent.createChooser(intentShare, null)
                    startActivity(chooser)
                }

                override fun onDeleteClickListener(event: Event) {
                    viewModel.deleteById(event.id)
                }

                override fun onEditClickListener(event: Event) {
                    editEventViewModel.update(event)
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(R.id.action_bottomNavigationFragment_to_editEventFragment)
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
            EditEventFragment.EVENT_UPDATED,
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

                adapter.submitList(state.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

}