package com.eltex.androidschool.store

import com.eltex.androidschool.effecthandler.EventEffectHandler
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.reducer.EventReducer
import com.eltex.androidschool.viewmodel.EventStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class EventStoreModule {

    @Provides
    fun provideEventStore(
        eventReducer: EventReducer,
        eventEffectHandler: EventEffectHandler,
    ): EventStore = EventStore(
        eventReducer,
        eventEffectHandler,
        setOf(EventMessage.Refresh),
        EventUiState(),
    )
}