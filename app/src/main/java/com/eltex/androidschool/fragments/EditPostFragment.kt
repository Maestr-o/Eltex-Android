package com.eltex.androidschool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEditPostBinding
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.EditPostViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditPostFragment : Fragment() {

    companion object {
        const val POST_UPDATED = "POST_UPDATED"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    lateinit var binding: FragmentEditPostBinding

    override fun onStart() {
        super.onStart()
        toolbarViewModel.showSave(true)
        toolbarViewModel.updateTitle(getString(R.string.edit_post))
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
        binding = FragmentEditPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editPostViewModel by activityViewModels<EditPostViewModel>()

        editPostViewModel.state.onEach { ui ->
            if (ui.result != null) {
                binding.content.setText(ui.result.content)
            }

            (ui.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()
            }

            editPostViewModel.consumeError()
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()

                if (content.isNotBlank()) {
                    editPostViewModel.editById(content)
                    requireActivity().supportFragmentManager.setFragmentResult(
                        POST_UPDATED,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                } else {
                    requireContext().toast(R.string.empty_error, true)
                }

                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}