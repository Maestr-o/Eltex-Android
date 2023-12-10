package com.eltex.androidschool.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.eltex.androidschool.dao.PostsDaoImpl

class AppDb private constructor(db: SQLiteDatabase) {

    val postsDao = PostsDaoImpl(db)

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            INSTANCE?.let { return it }

            val applicationContext = context.applicationContext

            synchronized(this) {
                INSTANCE?.let { return it }
                val appDb = AppDb(
                    DbHelper(applicationContext).writableDatabase
                )
                INSTANCE = appDb
                return appDb
            }
        }
    }
}