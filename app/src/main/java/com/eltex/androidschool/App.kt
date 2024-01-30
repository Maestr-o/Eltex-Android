package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.di.AppComponent
import com.eltex.androidschool.di.DependencyContainer
import com.eltex.androidschool.di.DependencyContainerImpl

class App : Application(), AppComponent {

    private val container by lazy {
        DependencyContainerImpl(this)
    }

    override fun getContainer(): DependencyContainer = container
}