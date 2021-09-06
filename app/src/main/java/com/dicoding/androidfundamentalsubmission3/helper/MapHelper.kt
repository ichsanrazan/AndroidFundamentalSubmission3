package com.dicoding.androidfundamentalsubmission3.helper

import android.database.Cursor
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import com.dicoding.androidfundamentalsubmission3.db.DatabaseContract

object MapHelper {

    fun mapUserCursorToArrayList(userCursor: Cursor?): ArrayList<UserGithub> {
        val userArray = ArrayList<UserGithub>()
        userCursor?.moveToFirst()
        if (userCursor != null) {
            if (userCursor.isFirst) {
                do {
                    val username = userCursor.getString(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME)
                    )
                    val avatar = userCursor.getInt(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR)
                    )
                    val avatarUrl = userCursor.getString(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL)
                    )
                    val name = userCursor.getString(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME)
                    )
                    val repository = userCursor.getInt(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY_COUNT)
                    )
                    val follower = userCursor.getInt(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWER_COUNT)
                    )
                    val following = userCursor.getInt(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING_COUNT)
                    )
                    val company = userCursor.getString(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY)
                    )
                    val location = userCursor.getString(
                            userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION)
                    )
                    userArray.add(
                            UserGithub(
                                    username,
                                    name,
                                    avatar,
                                    avatarUrl,
                                    company,
                                    location,
                                    repository,
                                    follower,
                                    following
                            )
                    )
                } while (userCursor.moveToNext())
            }
        }
        return userArray
    }
}