package com.dicoding.androidfundamentalsubmission3.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.androidfundamentalsubmission3.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.dicoding.androidfundamentalsubmission3.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.dicoding.androidfundamentalsubmission3.db.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper(mContext: Context) {

    companion object {

        private const val DATABASE_TABLE = TABLE_NAME

        private lateinit var mDatabaseHelper: DatabaseHelper
        private lateinit var mDatabase: SQLiteDatabase
        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: UserHelper(context)
                }
    }

    init {
        mDatabaseHelper = DatabaseHelper(mContext)
    }

    @Throws(SQLException::class)
    fun open() {
        mDatabase = mDatabaseHelper.writableDatabase
    }

    fun close() {
        mDatabaseHelper.close()
        if (mDatabase.isOpen)
            mDatabase.close()
    }

    fun queryAll(): Cursor {
        return mDatabase.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC",
                null
        )
    }

    fun queryByUsername(username: String): Cursor {
        return mDatabase.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(username),
                null,
                null,
                null,
                null
        )
    }

    fun queryById(id: String): Cursor {
        return mDatabase.query(
                DATABASE_TABLE,
                null,
                "$_ID = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )
    }

    fun insert(values: ContentValues?): Long {
        return mDatabase.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String): Int {
        return mDatabase.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }

    fun deleteById(id: String): Int {
        return mDatabase.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}