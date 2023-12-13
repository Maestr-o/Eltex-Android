package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eltex.androidschool.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM Posts ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Upsert
    fun save(post: PostEntity): Long

    @Query(
        """
            UPDATE Posts SET 
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END 
            WHERE id = :id;
        """
    )

    fun likeById(id: Long)

    @Query("DELETE FROM Posts WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM Posts WHERE id = :id")
    fun getPostById(id: Long): PostEntity
}