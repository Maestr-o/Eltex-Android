package com.eltex.androidschool.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, "app_db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE ${PostTable.TABLE_NAME} (
            	${PostTable.ID}	INTEGER PRIMARY KEY AUTOINCREMENT,
            	${PostTable.CONTENT}	TEXT NOT NULL,
            	${PostTable.PUBLISHED}	TEXT NOT NULL,
            	${PostTable.AUTHOR}	TEXT NOT NULL,
            	${PostTable.LIKED_BY_ME}	INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}