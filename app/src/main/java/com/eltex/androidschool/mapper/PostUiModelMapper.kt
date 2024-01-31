package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.PostUiModel
import java.time.Instant
import javax.inject.Inject

class PostUiModelMapper @Inject constructor(
    private val dtfProvider: DateTimeFormatterProvider,
) {

    fun map(post: Post): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorJob = authorJob,
            authorAvatar = authorAvatar,
            content = content,
            published = dtfProvider.format(Instant.now()),
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            attachment = attachment,
        )
    }
}