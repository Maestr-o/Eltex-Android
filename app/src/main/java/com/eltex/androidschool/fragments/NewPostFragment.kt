package com.eltex.androidschool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEditPostBinding
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.NetworkPostRepository
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.NewPostViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewPostFragment : Fragment() {

    companion object {
        const val POST_UPDATED = "POST_UPDATED"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.showSave(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.showSave(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(inflater, container, false)

        val viewModel by viewModels<NewPostViewModel> {
            viewModelFactory {
                initializer {
                    NewPostViewModel(repository = NetworkPostRepository())
                }
            }
        }

        viewModel.state.onEach { state ->
            if (state.result != null) {
                requireActivity().supportFragmentManager.setFragmentResult(POST_UPDATED, bundleOf())
                findNavController().navigateUp()
            }

            (state.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()
            }

            viewModel.consumeError()
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()

                if (content.isNotBlank()) {
                    viewModel.save(content)
                } else {
                    requireContext().toast(R.string.empty_error, true)
                }
                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

}