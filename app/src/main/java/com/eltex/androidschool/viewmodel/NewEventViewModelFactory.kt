package com.eltex.androidschool.viewmodel

import dagger.assisted.AssistedFactory

@AssistedFactory
interface NewEventViewModelFactory {
    fun create(eventId: Long): NewEventViewModel
}