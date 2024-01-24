package com.eltex.androidschool.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

        val newPostListener = View.OnClickListener {
            findNavController().navigate(R.id.action_bottomNavigationFragment_to_newPostFragment)
        }

        val newEventListener = View.OnClickListener {
            findNavController().navigate(R.id.action_bottomNavigationFragment_to_newEventFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postsFragment -> {
                    binding.newCard.setOnClickListener(newPostListener)
                    binding.newCard.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.eventsFragment -> {
                    binding.newCard.setOnClickListener(newEventListener)
                    binding.newCard.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.usersFragment -> {
                    binding.bottomNavigation.setOnClickListener(null)
                    binding.newCard.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }

        return binding.root
    }

}