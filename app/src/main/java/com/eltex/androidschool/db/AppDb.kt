package com.eltex.androidschool.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eltex.androidschool.dao.EventsDao
import com.eltex.androidschool.dao.PostsDao
import com.eltex.androidschool.entity.EventEntity
import com.eltex.androidschool.entity.PostEntity

@Database(
    entities = [PostEntity::class, EventEntity::class],
    version = 1,
)
abstract class AppDb : RoomDatabase() {

    abstract val postsDao: PostsDao
    abstract val eventsDao: EventsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            INSTANCE?.let { return it }
            val application = context.applicationContext
            synchronized(this) {
                INSTANCE?.let { return it }
                val appDb = Room.databaseBuilder(application, AppDb::class.java, "app_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = appDb
                return appDb
            }
        }
    }
}