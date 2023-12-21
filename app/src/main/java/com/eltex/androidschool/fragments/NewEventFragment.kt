package com.eltex.androidschool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEditEventBinding
import com.eltex.androidschool.db.AppDb
import com.eltex.androidschool.repository.SQLiteEventRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewEventFragment : Fragment() {

    companion object {
        const val ARG_EVENT_ID = "ARG_EVENT_ID"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    lateinit var binding: FragmentEditEventBinding

    override fun onStart() {
        super.onStart()
        toolbarViewModel.showSave(true)
        toolbarViewModel.updateTitle(getString(R.string.new_event))
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.showSave(false)
        toolbarViewModel.updateTitle(getString(R.string.app_name))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getLong(ARG_EVENT_ID) ?: 0L
        val newEventViewModel by viewModels<NewEventViewModel> {
            viewModelFactory {
                initializer {
                    NewEventViewModel(
                        repository = SQLiteEventRepository(
                            AppDb.getInstance(
                                requireContext().applicationContext
                            ).eventsDao
                        ),
                        id = id,
                    )
                }
            }
        }

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()
                if (content.isNotBlank()) {
                    newEventViewModel.save(content)
                    findNavController().navigateUp()
                } else {
                    requireContext().toast(R.string.empty_error, true)
                }
                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}