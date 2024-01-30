package com.eltex.androidschool.utils

import androidx.fragment.app.Fragment
import com.eltex.androidschool.di.AppComponent

fun Fragment.getDependencyContainer() =
    (requireContext().applicationContext as AppComponent).getContainer()