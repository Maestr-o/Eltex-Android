package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import java.time.Instant
import javax.inject.Inject

class EventUiModelMapper @Inject constructor(
    private val dtfProvider: DateTimeFormatterProvider,
) {

    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorJob = authorJob,
            authorAvatar = authorAvatar,
            content = content,
            datetime = dtfProvider.format(Instant.now()),
            published = dtfProvider.format(Instant.now()),
            type = type,
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            link = link,
            likes = likeOwnerIds.size,
            participants = participantsIds.size,
            attachment = attachment,
        )
    }
}