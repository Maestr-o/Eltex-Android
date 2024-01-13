package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.MainDispatcherRule
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class EventViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `deleteById error then error in state`() {
        val testError = RuntimeException("Test error")
        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun unlikeById(id: Long): Event = error("Not implemented")

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = throw testError
            },
        )
        viewModel.deleteById(111)
        TestCase.assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }
}