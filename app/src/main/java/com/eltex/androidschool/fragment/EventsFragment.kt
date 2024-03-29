package com.eltex.androidschool.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.effecthandler.EventEffectHandler
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.mapper.EventPagingModelMapper
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.reducer.EventReducer
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.onScrollToBottom
import com.eltex.androidschool.viewmodel.EventStore
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
                    EventViewModel(
                        EventStore(
                            reducer = EventReducer(),
                            effectHandler = EventEffectHandler(
                                NetworkEventRepository(
                                    EventsApi.INSTANCE,
                                    MediaApi.INSTANCE,
                                    requireContext().contentResolver,
                                ),
                                EventUiModelMapper()
                            ),
                            initMessages = setOf(EventMessage.Refresh),
                            initState = EventUiState(),
                        ),
                    )
                }
            }
        }

        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Like(event))
                }

                override fun onParticipateClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Participate(event))
                }

                override fun onShareClickListener(event: EventUiModel) {
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

                override fun onDeleteClickListener(event: EventUiModel) {
                    viewModel.accept(EventMessage.Delete(event))
                }

                override fun onEditClickListener(event: EventUiModel) {
                    val bundle = bundleOf(EditEventFragment.EDITING_EVENT to event)
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(R.id.action_bottomNavigationFragment_to_editEventFragment, bundle)
                }

                override fun onRetryPageClickListener() {
                    viewModel.accept(EventMessage.Retry)
                }
            }
        )

        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(EventMessage.Refresh)
        }

        binding.retryButton.setOnClickListener {
            viewModel.accept(EventMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            EditEventFragment.EVENT_UPDATED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(EventMessage.Refresh)
        }

        binding.list.onScrollToBottom {
            viewModel.accept(EventMessage.LoadNextPage)
        }

        val mapper = EventPagingModelMapper()

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
                    viewModel.accept(EventMessage.HandleError)
                }
                adapter.submitList(mapper.map(state))
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}