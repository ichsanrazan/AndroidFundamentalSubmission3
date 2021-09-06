package com.dicoding.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.androidfundamentalsubmission3"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val AVATAR_URL = "avatar_url"
            const val NAME = "name"
            const val REPOSITORY_COUNT = "repository_count"
            const val FOLLOWER_COUNT = "follower_count"
            const val FOLLOWING_COUNT = "following_count"
            const val COMPANY = "company"
            const val LOCATION = "location"

            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}