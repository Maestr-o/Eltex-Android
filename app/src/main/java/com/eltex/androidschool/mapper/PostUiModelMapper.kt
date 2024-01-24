package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.PostUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PostUiModelMapper {
    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(post: Post): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorJob = authorJob,
            authorAvatar = authorAvatar,
            content = content,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
        )
    }
}