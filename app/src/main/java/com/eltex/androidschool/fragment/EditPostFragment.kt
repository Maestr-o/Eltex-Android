package com.eltex.androidschool.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEditPostBinding
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.EditPostViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

class EditPostFragment : Fragment() {

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

        val viewModel by activityViewModels<EditPostViewModel>()

        binding.content.setText(viewModel.state.value.result?.content)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                viewModel.setFile(FileModel(it, AttachmentType.IMAGE))
            }
        }

        val imageUri = createFileUri()
        val takePhoto =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success != null) {
                    viewModel.setFile(FileModel(imageUri, AttachmentType.IMAGE))
                }
            }

        binding.attach.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.takePhoto.setOnClickListener {
            takePhoto.launch(imageUri)
        }

        binding.remove.setOnClickListener {
            viewModel.setFile(null)
        }

        binding.geo.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.not_implemented),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.user.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.not_implemented),
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.state.onEach { state ->
            val file = state.file
            when (file?.type) {
                AttachmentType.IMAGE -> {
                    binding.photoContainer.isVisible = true
                    Glide.with(binding.photo)
                        .load(file.uri)
                        .into(binding.photo)
                }

                AttachmentType.AUDIO,
                AttachmentType.VIDEO,
                null -> binding.photoContainer.isGone = true
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
                    viewModel.editById(content)
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

        return binding.root
    }

    private fun createFileUri(): Uri {
        val directory = requireContext().cacheDir.resolve("file_picker").apply {
            mkdirs()
        }

        val file = File(directory, "image.png")

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }
}