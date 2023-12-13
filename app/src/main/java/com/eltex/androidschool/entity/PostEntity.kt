package com.eltex.androidschool.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.model.Post

@Entity(tableName = "Posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "authorId")
    val authorId: Long = 0L,
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "authorJob")
    val authorJob: String? = null,
    @ColumnInfo(name = "authorAvatar")
    val authorAvatar: String? = null,
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "link")
    val link: String? = null,
    @ColumnInfo(name = "mentionedMe")
    val mentionedMe: Boolean = false,
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
) {
    companion object {
        fun fromPost(post: Post): PostEntity = with(post) {
            PostEntity(
                id = id,
                authorId = authorId,
                author = author,
                authorJob = authorJob,
                authorAvatar = authorAvatar,
                content = content,
                published = published,
                link = link,
                mentionedMe = mentionedMe,
                likedByMe = likedByMe,
            )
        }
    }

    fun toPost(): Post = Post(
        id = id,
        authorId = authorId,
        author = author,
        authorJob = authorJob,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        link = link,
        mentionedMe = mentionedMe,
        likedByMe = likedByMe,
    )
}