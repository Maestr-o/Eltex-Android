package com.eltex.androidschool.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventType
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val repository: EventRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    fun update(event: EventUiModel) {
        _state.update {
            it.copy(
                result = Event(
                    id = event.id,
                    content = event.content,
                    likedByMe = event.likedByMe,
                    type = EventType.ONLINE,
                    datetime = Instant.now(),
                    participatedByMe = event.participatedByMe,
                ),
                file = null,
            )
        }
        val attachment = event.attachment
        if (attachment != null) {
            _state.update {
                it.copy(
                    file = FileModel(
                        attachment.url.toUri(),
                        attachment.attachmentType,
                    )
                )
            }
        }
    }

    fun editById(content: String) {
        _state.update {
            it.copy(status = Status.Loading)
        }

        val event = state.value.result
        if (event != null) {
            viewModelScope.launch {
                try {
                    val data = repository.saveEvent(event.id, content, state.value.file)
                    _state.update { ui ->
                        ui.copy(result = data, status = Status.Idle)
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(status = Status.Error(e))
                    }
                }
            }
        }
    }

    fun setFile(file: FileModel?) {
        _state.update {
            it.copy(file = file)
        }
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}