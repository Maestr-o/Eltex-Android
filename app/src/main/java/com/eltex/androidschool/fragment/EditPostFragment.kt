package com.eltex.androidschool.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEditPostBinding
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.NewPostViewModel
import com.eltex.androidschool.viewmodel.NewPostViewModelFactory
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

@AndroidEntryPoint
class EditPostFragment : Fragment() {

    companion object {
        const val POST_UPDATED = "POST_UPDATED"
        const val EDITING_POST = "EDITING_POST"
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

        val post = arguments?.getSerializable(EDITING_POST) as PostUiModel
        binding.content.setText(post.content)

        val viewModel by viewModels<NewPostViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<NewPostViewModelFactory> { factory ->
                    factory.create(post.id)
                }
            }
        )

        if (savedInstanceState == null) {
            if (post.attachment != null) {
                viewModel.setFile(
                    FileModel(
                        post.attachment.url.toUri(),
                        post.attachment.attachmentType
                    )
                )
            }
        }

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
            if (state.result != null) {
                requireActivity().supportFragmentManager.setFragmentResult(
                    NewPostFragment.POST_UPDATED,
                    bundleOf()
                )
                findNavController().navigateUp()
            }

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
                    viewModel.save(content)
                    requireActivity().supportFragmentManager.setFragmentResult(
                        POST_UPDATED,
                        bundleOf()
                    )
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