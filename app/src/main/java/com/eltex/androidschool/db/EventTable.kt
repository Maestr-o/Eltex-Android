package com.eltex.androidschool.db

object EventTable {
    const val TABLE_NAME = "Events"
    const val ID = "ID"
    const val AUTHOR = "AUTHOR"
    const val CONTENT = "CONTENT"
    const val DATETIME = "DATETIME"
    const val PUBLISHED = "PUBLISHED"
    const val LIKED_BY_ME = "LIKED_BY_ME"
    const val PARTICIPATED_BY_ME = "PARTICIPATED_BY_ME"
    val allColumns = arrayOf(
        ID,
        AUTHOR,
        CONTENT,
        DATETIME,
        PUBLISHED,
        LIKED_BY_ME,
        PARTICIPATED_BY_ME
    )
}