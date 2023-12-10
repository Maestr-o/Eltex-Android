package com.eltex.androidschool.db

object PostTable {
    const val TABLE_NAME = "Posts"
    const val ID = "id"
    const val AUTHOR = "author"
    const val CONTENT = "content"
    const val PUBLISHED = "published"
    const val LIKED_BY_ME = "likedByMe"
    val allColumns = arrayOf(ID, AUTHOR, CONTENT, PUBLISHED, LIKED_BY_ME)
}