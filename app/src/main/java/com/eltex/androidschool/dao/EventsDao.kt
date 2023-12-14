package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eltex.androidschool.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM Events ORDER BY id DESC")
    fun getAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM Events WHERE id = :id")
    fun getEventById(id: Long): EventEntity

    @Upsert
    fun save(event: EventEntity)

    @Query("DELETE FROM Events WHERE id = :id")
    fun deleteById(id: Long)

    @Query(
        """
            UPDATE Events SET 
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END 
            WHERE id = :id;
        """
    )
    fun likeById(id: Long)

    @Query(
        """
            UPDATE Events SET 
                participatedByMe = CASE WHEN participatedByMe THEN 0 ELSE 1 END 
            WHERE id = :id;
        """
    )
    fun participatedById(id: Long)
}