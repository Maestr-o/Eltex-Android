package com.eltex.androidschool.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentToolbarBinding
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ToolbarFragment : Fragment() {

    lateinit var binding: FragmentToolbarBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentFragmentManager.beginTransaction()
            .setPrimaryNavigationFragment(this)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToolbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()
        binding.toolbar.setupWithNavController(navController)

        val toolbarViewModel by activityViewModels<ToolbarViewModel>()

        val item = binding.toolbar.menu.findItem(R.id.save)

        toolbarViewModel.showSave
            .onEach {
                item.isVisible = it
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolbarViewModel.title
            .onEach {
                binding.toolbar.title = it
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        item.setOnMenuItemClickListener {
            toolbarViewModel.saveClicked(true)
            true
        }
    }
}