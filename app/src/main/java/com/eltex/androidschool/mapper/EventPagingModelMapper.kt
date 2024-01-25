package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.model.NoteStatus
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.reducer.EventReducer

class EventPagingModelMapper {

    fun map(eventUiState: EventUiState): List<PagingModel<EventUiModel>> {
        val events: List<PagingModel.Data<EventUiModel>> = eventUiState.events.map {
            PagingModel.Data(it)
        }

        return when (val status = eventUiState.status) {
            is NoteStatus.NextPageError -> events + PagingModel.Error(status.reason)
            is NoteStatus.NextPageLoading -> events + List(EventReducer.PAGE_SIZE) { PagingModel.Progress }
            NoteStatus.EmptyLoading -> List(EventReducer.INITIAL_LOAD_SIZE) { PagingModel.Progress }
            else -> events
        }
    }
}