package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventUiModelMapper {
    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorJob = authorJob,
            authorAvatar = authorAvatar,
            content = content,
            datetime = FORMATTER.format(datetime.atZone(ZoneId.systemDefault())),
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            coords = coords,
            type = type,
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            attachment = attachment,
            link = link,
            likes = likeOwnerIds.size,
            participants = participantsIds.size,
        )
    }
}