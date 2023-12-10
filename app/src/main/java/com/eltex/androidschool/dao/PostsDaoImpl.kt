package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import com.eltex.androidschool.db.PostTable
import com.eltex.androidschool.model.Post

class PostsDaoImpl(private val db: SQLiteDatabase) : PostsDao {
    override fun getAll(): List<Post> =
        db.query(
            PostTable.TABLE_NAME,
            PostTable.allColumns,
            null,
            null,
            null,
            null,
            "${PostTable.ID} DESC"
        ).use { cursor ->
            val result = mutableListOf<Post>()
            while (cursor.moveToNext()) {
                result += cursor.getPost()
            }
            result
        }

    override fun save(post: Post): Post {
        val contentValues = contentValuesOf(
            PostTable.AUTHOR to post.author,
            PostTable.CONTENT to post.content,
            PostTable.PUBLISHED to post.published,
            PostTable.LIKED_BY_ME to post.likedByMe
        )

        if (post.id != 0L) {
            contentValues.put(PostTable.ID, post.id)
        }

        val id = db.replace(PostTable.TABLE_NAME, null, contentValues)
        return getPostById(id)
    }

    private fun getPostById(id: Long): Post =
        db.query(
            PostTable.TABLE_NAME,
            PostTable.allColumns,
            "${PostTable.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.getPost()
        }

    override fun deleteById(id: Long) {
        db.delete(PostTable.TABLE_NAME, "${PostTable.ID}=?", arrayOf(id.toString()))
    }

    override fun likeById(id: Long): Post {
        db.execSQL(
            """
            UPDATE ${PostTable.TABLE_NAME} SET 
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END 
            WHERE id = ?;
            """.trimIndent(),
            arrayOf(id.toString())
        )
        return getPostById(id)
    }

    private fun Cursor.getPost(): Post =
        Post(
            id = getLong(getColumnIndexOrThrow(PostTable.ID)),
            author = getString(getColumnIndexOrThrow(PostTable.AUTHOR)),
            content = getString(getColumnIndexOrThrow(PostTable.CONTENT)),
            published = getString(getColumnIndexOrThrow(PostTable.PUBLISHED)),
            likedByMe = getInt(getColumnIndexOrThrow(PostTable.LIKED_BY_ME)) == 1,
        )
}